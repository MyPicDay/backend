package mypicday.store.fcm.repository;

import mypicday.store.fcm.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByUserIdAndDeviceId(String userId, String deviceId);

    List<FcmToken> findAllByUserId(String userId);
}

