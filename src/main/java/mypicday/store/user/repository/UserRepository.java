package mypicday.store.user.repository;

import mypicday.store.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    Page<User> findByNicknameContaining(String nickname, Pageable pageable);
}
