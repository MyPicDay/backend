package mypicday.store.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.notification.dto.NotificationDTO;
import mypicday.store.notification.service.NotificationService;
import mypicday.store.user.dto.response.UserSearchResponse;
import mypicday.store.user.entity.User;
import mypicday.store.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userService.getUserById(customUserDetails.getId());
        List<NotificationDTO> unreadNotifications = notificationService.findUnreadByUser(user);
        return ResponseEntity.ok(unreadNotifications);
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User user = userService.getUserById(customUserDetails.getId());
        List<NotificationDTO> allNotifications = notificationService.findAllByUser(user);
        return ResponseEntity.ok(allNotifications);
    }


    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable String notificationId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User user = userService.getUserById(customUserDetails.getId());
        notificationService.markAsRead(notificationId, user);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
