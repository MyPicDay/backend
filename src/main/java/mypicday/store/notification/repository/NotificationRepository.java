package mypicday.store.notification.repository;

import mypicday.store.notification.entity.Notification;
import mypicday.store.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByReceiverAndIsReadFalse(User receiver);

    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);

}
