package mypicday.store.diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.global.entity.BaseEntity;
import mypicday.store.user.entity.User;


import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

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

}
