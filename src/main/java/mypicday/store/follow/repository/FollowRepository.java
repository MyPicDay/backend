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

    List<Follow> findByFollower_Id(String followerId);
    List<Follow> findByFollowing_Id(String followingId);

    long countByFollower_Id(String userId); // 나를 팔로우한 사람들 (followers)
    long countByFollowing_Id(String userId);  // 내가 팔로우한 사람들 (followings)
}
