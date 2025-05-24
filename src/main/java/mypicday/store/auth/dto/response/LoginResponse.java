package mypicday.store.auth.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.auth.dto.UserInfo;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {
    private String message;
    private UserInfo user;

    public LoginResponse(String message, UserInfo user) {
        this.message = message;
        this.user = user;
    }

}
