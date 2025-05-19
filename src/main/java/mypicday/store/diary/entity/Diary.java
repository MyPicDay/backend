package mypicday.store.diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.global.entity.BaseEntity;
import mypicday.store.user.entity.User;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "diaries")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    private String content;

    @Enumerated(STRING)
    private Status status;

    private String MainImageUrl ;

    private String imageUrl1;

    private String imageUrl2;

   // TODO (추후 수정 예정) 임시 생성용 public 생성자
    public Diary(User user, String title, String content, Status status, String mainImageUrl, String imageUrl1, String imageUrl2) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status;
        MainImageUrl = mainImageUrl;
        this.imageUrl1 = imageUrl1;
        this.imageUrl2 = imageUrl2;
    }
}
