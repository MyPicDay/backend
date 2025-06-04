package mypicday.store.fcm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.fcm.service.FcmTokenService;
import mypicday.store.global.config.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmTokenService fcmTokenService;

    @PostMapping("/token")
    public ResponseEntity<Void> saveFcmToken(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestBody Map<String, String> body) {
        String fcmToken = body.get("token");
        String deviceId = body.get("deviceId");

        fcmTokenService.register(
                userDetails.getId(),
                deviceId,
                fcmToken
        );
        log.info("사용자 {}의 FCM 토큰 저장 요청: {}", userDetails.getId(), fcmToken);
        return ResponseEntity.ok().build();
    }
}
