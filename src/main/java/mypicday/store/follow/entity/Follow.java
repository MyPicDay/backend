package mypicday.store.follow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mypicday.store.user.entity.User;

@Getter
@Entity
@Table(name = "follows")
public class Follow {
    //복합키 클래스를 엔티티의 PK로 사용
    @EmbeddedId
    private FollowId followerId;

    @ManyToOne
    //복합 키의 필드와 연관관계를 맵핑함(FollowId.followerId)
    @MapsId("followerId")
    //해당 필드를 foreign key로 맵핑함
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @MapsId("followingId")
    @JoinColumn(name = "following_id")
    private User following;

    public Follow(User follower, User following) {
        this.followerId = new FollowId(follower.getId(), following.getId());
        this.follower = follower;
        this.following = following;
    }

    protected Follow() {
    }

}
