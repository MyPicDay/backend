package mypicday.store.follow.repository;

import mypicday.store.follow.entity.Follow;
import mypicday.store.follow.entity.FollowId;
import mypicday.store.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {
    //
    boolean existsByFollowerAndFollowing(User follower, User following);


    void deleteByFollowerAndFollowing(User follower, User following);
}
