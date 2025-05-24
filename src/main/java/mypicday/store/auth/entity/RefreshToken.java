package mypicday.store.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tokens")
@Getter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @Column(name = "refresh_key")
    private String key; // email + deviceId 조합

    private String refreshToken;

    public RefreshToken(String key, String refreshToken) {
        this.key = key;
        this.refreshToken = refreshToken;
    }

    public void updateToken(String newToken) {
        this.refreshToken = newToken;
    }
}
