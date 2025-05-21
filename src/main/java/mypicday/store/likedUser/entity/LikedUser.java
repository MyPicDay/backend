package mypicday.store.likedUser.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.global.entity.BaseEntity;

import mypicday.store.like.entity.LikeEntity;
import mypicday.store.user.entity.User;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikedUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "LikedUser_id")
    private Long id;

    private boolean liked;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "like_id")
    private LikeEntity like ;

    public LikedUser(boolean liked, User user, LikeEntity like) {
        this.liked = liked;
        this.user = user;
        this.like = like;
    }

    public static LikedUser of(boolean liked,User user , LikeEntity like) {
        return new LikedUser(liked , user , like);
    }

    public void changeLiked(){
        this.liked = !this.liked ;
    }
}
