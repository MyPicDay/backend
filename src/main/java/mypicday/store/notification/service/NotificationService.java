package mypicday.store.notification.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.diary.entity.Diary;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.notification.dto.NotificationDTO;
import mypicday.store.notification.entity.Notification;
import mypicday.store.notification.repository.NotificationRepository;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import mypicday.store.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    // NotificationService.java

    public List<NotificationDTO> findUnreadByUser(User user) {
        List<Notification> notifications = notificationRepository.findByReceiverAndIsReadFalse(user);

        return notifications.stream()
                .map(notification -> {
                    String senderProfileImage = null;
                    String diaryThumbnail = null;
                    String moveToWhere = null;

                    // sender 정보 조회
                    if (notification.getSenderId() != null) {
                        User sender = userRepository.findById(notification.getSenderId()).orElse(null);
                        if (sender != null) {
                            senderProfileImage = sender.getAvatar();
                        }
                    }

                    String type = notification.getType();

                    if (type != null) {
                        switch (type) {
                            case "LIKE":
                            case "COMMENT":
                            case "REPLY":
                                if (notification.getReferenceId() != null) {
                                    try {
                                        Long diaryId = Long.parseLong(notification.getReferenceId());
                                        Diary diary = diaryRepository.findById(diaryId).orElse(null);
                                        List<String> imageList = diary.getImageList();
                                        if (imageList != null && !imageList.isEmpty()) {
                                            diaryThumbnail = imageList.get(0);
                                        }
                                        moveToWhere = String.valueOf(diaryId);
                                    } catch (NumberFormatException e) {
                                        // referenceId가 Long이 아닐 경우 예외 방지
                                        e.printStackTrace();
                                    }
                                }
                                break;

                            case "FOLLOW":
                                if (notification.getSenderId() != null) {
                                    moveToWhere = notification.getSenderId();
                                }
                                break;

                            default:
                                moveToWhere = "/";
                                break;
                        }
                    }

                    return NotificationDTO.from(notification, senderProfileImage, diaryThumbnail, moveToWhere);
                })
                .toList();
    }


    public List<NotificationDTO> findAllByUser(User user) {
        List<Notification> notifications = notificationRepository.findByReceiverOrderByCreatedAtDesc(user);

        return notifications.stream()
                .map(notification -> {
                    String senderProfileImage = null;
                    String diaryThumbnail = null;
                    String moveToWhere = null;

                    // sender 정보 조회
                    if (notification.getSenderId() != null) {
                        User sender = userRepository.findById(notification.getSenderId()).orElse(null);
                        if (sender != null) {
                            senderProfileImage = sender.getAvatar();
                        }
                    }

                    String type = notification.getType();

                    if (type != null) {
                        switch (type) {
                            case "LIKE":
                            case "COMMENT":
                            case "REPLY":
                                if (notification.getReferenceId() != null) {
                                    try {
                                        Long diaryId = Long.parseLong(notification.getReferenceId());
                                        Diary diary = diaryRepository.findById(diaryId).orElse(null);
                                        List<String> imageList = diary.getImageList();
                                        if (imageList != null && !imageList.isEmpty()) {
                                            diaryThumbnail = imageList.get(0);
                                        }
                                        moveToWhere = String.valueOf(diaryId);
                                    } catch (NumberFormatException e) {
                                        // referenceId가 Long이 아닐 경우 예외 방지
                                        e.printStackTrace();
                                    }
                                }
                                break;

                            case "FOLLOW":
                                if (notification.getSenderId() != null) {
                                    moveToWhere = notification.getSenderId();
                                }
                                break;

                            default:
                                moveToWhere = "/";
                                break;
                        }
                    }

                    return NotificationDTO.from(notification, senderProfileImage, diaryThumbnail, moveToWhere);
                })
                .toList();
    }


    public void markAsRead(String notificationId, User user) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("해당 알림을 찾을 수 없습니다."));

        if (!notification.getReceiver().getId().equals(user.getId())) {
            throw new SecurityException("본인의 알림만 읽음 처리할 수 있습니다.");
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

}
