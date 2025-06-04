package mypicday.store.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.fcm.entity.FcmToken;
import mypicday.store.fcm.repository.FcmTokenRepository;
import mypicday.store.notification.dto.NotificationDTO;
import mypicday.store.notification.entity.Notification;
import mypicday.store.notification.repository.NotificationRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, ScheduledExecutorService> pingExecutors = new ConcurrentHashMap<>();

    public List<NotificationDTO> findAllByUser(User user) {
        log.info("[알림 조회] 사용자 ID={}의 알림 목록 조회", user.getId());

        List<Notification> notifications = notificationRepository.findByReceiverOrderByCreatedAtDesc(user);
        log.info("[알림 조회] {}건의 알림이 조회되었습니다.", notifications.size());

        return notifications.stream()
                .map(notification -> {
                    String senderProfileImage = null;
                    String diaryThumbnail = null;
                    String moveToWhere = null;

                    if (notification.getSenderId() != null) {
                        User sender = userRepository.findById(notification.getSenderId()).orElse(null);
                        if (sender != null) {
                            senderProfileImage = sender.getAvatar();
                        } else {
                            log.warn("보낸 사람(senderId={})을 찾을 수 없습니다.", notification.getSenderId());
                        }
                    }

                    String type = notification.getType();

                    if (type != null) {
                        switch (type) {
                            case "LIKE", "COMMENT", "REPLY" -> {
                                if (notification.getReferenceId() != null) {
                                    try {
                                        Long diaryId = Long.parseLong(notification.getReferenceId());
                                        Diary diary = diaryRepository.findById(diaryId).orElse(null);
                                        if (diary != null && diary.getImageList() != null && !diary.getImageList().isEmpty()) {
                                            diaryThumbnail = diary.getImageList().get(0);
                                        }
                                        moveToWhere = String.valueOf(diaryId);
                                    } catch (NumberFormatException e) {
                                        log.error("referenceId가 숫자가 아닙니다: {}", notification.getReferenceId(), e);
                                    }
                                }
                            }
                            case "FOLLOW" -> moveToWhere = notification.getSenderId();
                            default -> moveToWhere = "/";
                        }
                    }

                    return NotificationDTO.from(notification, senderProfileImage, diaryThumbnail, moveToWhere);
                })
                .toList();
    }

    public void markAsRead(String notificationId, User user) {
        log.info("[알림 읽음] 알림 ID={} 사용자의 알림 읽음 처리 요청, userId={}", notificationId, user.getId());

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> {
                    log.error("알림 ID={}를 찾을 수 없습니다.", notificationId);
                    return new IllegalArgumentException("해당 알림을 찾을 수 없습니다.");
                });

        if (!notification.getReceiver().getId().equals(user.getId())) {
            log.warn("읽음 처리 권한 없음: 요청자={}, 알림 수신자={}", user.getId(), notification.getReceiver().getId());
            throw new SecurityException("본인의 알림만 읽음 처리할 수 있습니다.");
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
            log.info("알림 ID={} 읽음 처리 완료", notificationId);
        } else {
            log.info("알림 ID={} 이미 읽음 상태입니다.", notificationId);
        }
    }

    public SseEmitter subscribe(String userId) {
        log.info("[SSE 연결] 사용자 ID={}의 SSE 구독 요청", userId);

        SseEmitter emitter = new SseEmitter(60 * 1000L * 60); // 60분 유지
        SseEmitter oldEmitter = emitters.put(userId, emitter);
        if (oldEmitter != null) {
            oldEmitter.complete();
            shutdownPingExecutor(userId);
            log.info("[SSE 중복 연결] 사용자 ID={}의 기존 SSE 연결 종료", userId);
        }

        Runnable cleanup = () -> {
            emitters.remove(userId);
            shutdownPingExecutor(userId);
        };

        emitter.onCompletion(cleanup);
        emitter.onTimeout(cleanup);
        emitter.onError(e -> {
            log.error("[SSE 오류] 사용자 ID={} SSE 오류 발생", userId, e);
            cleanup.run();
        });

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        pingExecutors.put(userId, executor);
        executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
                log.debug("[SSE 핑] 사용자 ID={}에게 ping 전송", userId);
            } catch (IOException e) {
                emitter.completeWithError(e);
                log.warn("[SSE 핑 실패] 사용자 ID={} ping 전송 실패", userId);
            }
        }, 0, 30, TimeUnit.SECONDS);

        int unreadCount = notificationRepository.countByReceiver_IdAndIsReadFalse(userId);
        try {
            emitter.send(SseEmitter.event().name("connect").data(unreadCount));
            log.info("[SSE 연결 완료] 사용자 ID={}에게 초기 connect 이벤트 전송 완료", userId);
        } catch (IOException e) {
            cleanup.run();
            log.error("[SSE 연결 실패] 사용자 ID={} 초기 데이터 전송 실패", userId, e);
        }

        return emitter;
    }


    private void shutdownPingExecutor(String userId) {
        ScheduledExecutorService executor = pingExecutors.remove(userId);
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
        }
    }

    public void sendNotification(String userId, NotificationDTO dto) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(dto));
                log.info("[SSE 알림 전송] 사용자 ID={}에게 알림 전송 완료", userId);
            } catch (IOException e) {
                emitters.remove(userId);
                shutdownPingExecutor(userId);
                log.error("[SSE 알림 전송 실패] 사용자 ID={} 알림 전송 중 오류 발생", userId, e);
            }
        } else {
            log.warn("[SSE 알림 전송 실패] 사용자 ID={}의 emitter가 존재하지 않습니다.", userId);
        }
        sendFcmNotification(userId, dto);
    }

    private void sendFcmNotification(String userId, NotificationDTO dto) {
        List<FcmToken> tokens = fcmTokenRepository.findAllByUserId(userId);
        if (tokens.isEmpty()) {
            log.warn("[FCM] 사용자 {}의 등록된 디바이스 토큰이 없습니다.", userId);
            return;
        }

        for (FcmToken token : tokens) {
            Message message = Message.builder()
                    .setToken(token.getFcmToken())
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle("MyPicDay")
                            .setBody(dto.getMessage())
                            .build())
                    .putData("type", dto.getType())
                    .putData("targetId", dto.getReceiverId())
                    .build();

            try {
                String response = firebaseMessaging.send(message);
                log.info("[FCM] 사용자 {}에게 알림 전송 성공: {}", userId, response);
            } catch (FirebaseMessagingException e) {
                log.error("[FCM] 사용자 {}에게 알림 전송 실패", userId, e);
            }
        }
    }
}
