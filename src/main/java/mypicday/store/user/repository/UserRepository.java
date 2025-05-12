package mypicday.store.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mypicday.store.user.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findAllByJob(String job);
}
