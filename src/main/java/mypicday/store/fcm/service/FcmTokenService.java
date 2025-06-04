package mypicday.store.fcm.service;

import lombok.RequiredArgsConstructor;
import mypicday.store.fcm.entity.FcmToken;
import mypicday.store.fcm.repository.FcmTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    public void register(String userId, String deviceId, String fcmToken) {
        Optional<FcmToken> existing = fcmTokenRepository
                .findByUserIdAndDeviceId(userId, deviceId);

        if (existing.isPresent()) {
            // 토큰이 바뀌었을 경우 업데이트
            FcmToken token = existing.get();
            if (!token.getFcmToken().equals(fcmToken)) {
                token.setFcmToken(fcmToken);
                token.setRegisteredAt(LocalDateTime.now());
                fcmTokenRepository.save(token);
            }
        } else {
            // 새로 저장
            FcmToken token = new FcmToken();
            token.setUserId(userId);
            token.setDeviceId(deviceId);
            token.setFcmToken(fcmToken);
            token.setRegisteredAt(LocalDateTime.now());
            fcmTokenRepository.save(token);
        }
    }
}

