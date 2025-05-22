package mypicday.store.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mypicday.store.diary.entity.Diary;
import mypicday.store.user.entity.User;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity
@Table(name= "comments")
@NoArgsConstructor(access = PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY )
    public Long id ;

    @ManyToOne(fetch = LAZY)
    private User user ;

    @ManyToOne(fetch = LAZY)
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

    public static Comment createReply(User user, Diary diary, String context ,Comment parent) {
        return new Comment(user, diary, context, parent);
    }


}
