package mypicday.store.diary.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.global.entity.BaseEntity;
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


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    private String title;
    private String content;

    @Enumerated(STRING)
    private Visibility status;


    @ElementCollection
    @CollectionTable(name = "diary_images", joinColumns = @JoinColumn(name = "diary_id"))
    @Column(name = "image", length = 100000) //
    private List<String> imageList;

    public Diary(User user, String title, String content, Visibility status, List<String> imageList) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.status = status;
        this.imageList = imageList;
    }

    public static Diary crateDiary(User user, String title, String content, Visibility status, List<String> imageList) {
        return new Diary(user, title, content, status, imageList);
    }

}
