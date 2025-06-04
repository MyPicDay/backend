package mypicday.store.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mypicday.store.notification.entity.Notification;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private String id;
    private String receiverId;
    private String type;     // "LIKE", "COMMENT", "REPLY", "FOLLOW"
    private String message;
    private boolean isread;
    private LocalDateTime createdAt;
    private String moveToWhere;

    private String senderProfileImage;  // 왼쪽 프로필 사진
    private String diaryThumbnail;

    public static NotificationDTO from(Notification notification,
                                       String senderProfileImage,
                                       String diaryThumbnail,
                                       String moveToWhere) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .isread(notification.isRead())
                .receiverId(notification.getReceiver().getId())
                .senderProfileImage(senderProfileImage)
                .diaryThumbnail(diaryThumbnail)
                .moveToWhere(moveToWhere)
                .build();
    }



}