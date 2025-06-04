package mypicday.store.user.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private String userId;
    private String nickname;
    private String email;
    private String avatar;

    public UserResponse(String userId, String nickname, String email, String avatar) {
        this.userId = userId;
        this.nickname = nickname;
        this.email = email;
        this.avatar = avatar;
    }
}
