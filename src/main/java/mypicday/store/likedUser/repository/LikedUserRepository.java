package mypicday.store.likedUser.repository;



import mypicday.store.like.entity.LikeEntity;
import mypicday.store.likedUser.entity.LikedUser;
import mypicday.store.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface LikedUserRepository extends JpaRepository<LikedUser, Long> {

    @Query("SELECT lu FROM LikedUser lu WHERE lu.user = :user AND lu.like = :like")
    Optional<LikedUser> findByUserAndLike(@Param("user") User user, @Param("like") LikeEntity like);


}
