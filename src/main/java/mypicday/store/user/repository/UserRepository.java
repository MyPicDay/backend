package mypicday.store.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mypicday.store.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
