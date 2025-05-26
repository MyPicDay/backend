package mypicday.store.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.diary.entity.Diary;
import mypicday.store.global.entity.BaseEntity;
import mypicday.store.user.entity.User;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name= "comments")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY )
    @Column(name = "comment_id")
    public Long id ;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id")
    private User user ;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary ;

    private String context ;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent ;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>( );

    public Comment(User user, Diary diary, String context) {
        this.user = user;
        this.diary = diary;
        this.context = context;
    }

    public Comment(User user, Diary diary, String context, Comment parent) {
        this.user = user;
        this.diary = diary;
        this.context = context;
        this.parent = parent;
    }

    public static Comment createComment(User user, Diary diary, String context) {
        return new Comment(user, diary, context);
    }

    public static Comment createReply(User user, Diary diary, String context ,Comment parent ) {
        if(parent == null) {
            return new Comment(user, diary, context);
        }
        return new Comment(user, diary, context, parent);
    }


}
