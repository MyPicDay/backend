package mypicday.store.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.notification.dto.NotificationDTO;
import mypicday.store.notification.service.NotificationService;
import mypicday.store.user.entity.User;
import mypicday.store.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("[GET] /api/notifications - 사용자 전체 알림 조회 요청, userId={}", customUserDetails.getId());

        User user = userService.getUserById(customUserDetails.getId());
        List<NotificationDTO> allNotifications = notificationService.findAllByUser(user);

        log.info("알림 {}건 조회 완료", allNotifications.size());
        return ResponseEntity.ok(allNotifications);
    }

    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable String notificationId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        log.info("[POST] /api/notifications/{}/read - 알림 읽음 처리 요청, userId={}", notificationId, customUserDetails.getId());

        User user = userService.getUserById(customUserDetails.getId());
        notificationService.markAsRead(notificationId, user);

        log.info("알림 {} 읽음 처리 완료", notificationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Integer> getUnreadCount(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        log.info("[GET] /api/notifications/unread/count - 읽지 않은 알림 개수 조회 요청, userId={}", customUserDetails.getId());

        int unreadCount = notificationService.getUnreadCount(customUserDetails.getId());

        log.info("읽지 않은 알림 개수: {}", unreadCount);
        return ResponseEntity.ok(unreadCount);
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String userId = customUserDetails.getId();
        log.info("[SSE] /api/notifications/subscribe - SSE 연결 요청, userId={}", userId);

        SseEmitter emitter = notificationService.subscribe(userId);

        log.info("[SSE] SSE 연결 완료, userId={}", userId);
        return emitter;
    }
}
