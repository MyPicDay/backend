package mypicday.store.diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.global.entity.BaseEntity;
import mypicday.store.like.entity.LikeEntity;
import mypicday.store.user.entity.User;
import java.util.List;

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


    @ManyToOne(fetch = LAZY )
    @JoinColumn(name = "id")
    private User user;


    private String title;
    private String content;

    @Enumerated(STRING)
    private Visibility status;

    @OneToOne (fetch = LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "like_id")
    private LikeEntity like ;


    @ElementCollection
    @CollectionTable(name = "diary_images", joinColumns = @JoinColumn(name = "diary_id"))
    @Column(name = "image", length = 100000)
    private List<String> imageList;


    public Diary(String content, Visibility status, LikeEntity like, List<String> imageList, String title, User user) {
        this.content = content;
        this.status = status;
        this.like = like;
        this.imageList = imageList;
        this.title = title;
        this.user = user;
    }
}
