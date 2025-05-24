package mypicday.store.likedUser.repository;



import mypicday.store.like.entity.LikeEntity;
import mypicday.store.likedUser.entity.LikedUser;
import mypicday.store.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface LikedUserRepository extends JpaRepository<LikedUser, Long> {

    @Query("SELECT lu FROM LikedUser lu WHERE lu.user = :user AND lu.like = :like")
    Optional<LikedUser> findByUserAndLike(@Param("user") User user, @Param("like") LikeEntity like);


    List<LikedUser> findLikedUserByLiked(boolean liked);

    // 특정 user_id로 liked 값 조회
    @Query("SELECT l.liked FROM LikedUser l WHERE l.user.id = :userId and l.like.id = :likedId")
    Optional<Boolean> findLikedByUserId(@Param("userId") String userId , @Param("likedId") long likedId);
}
