package mypicday.store.fcm.dto;

import lombok.Data;

@Data
public class FcmRegisterRequest {
    private String deviceId;   // ex) localStorage.getItem("deviceId")
    private String fcmToken;
}
