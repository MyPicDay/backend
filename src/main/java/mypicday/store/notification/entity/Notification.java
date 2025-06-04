package mypicday.store.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import mypicday.store.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver; // 알림을 받는 유저

    private String type;     // "LIKE", "COMMENT", "REPLY", "FOLLOW"
    private String senderId;  // 알림을 보낸 유저 (프로필)
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private String referenceId; // diaryId 저장 (썸네일)

    public void setRead(boolean read) {
        this.isRead = read;
    }

}