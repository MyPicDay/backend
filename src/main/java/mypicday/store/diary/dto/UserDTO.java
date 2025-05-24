package mypicday.store.diary.dto;

import lombok.Getter;
import mypicday.store.user.entity.User;

@Getter
public class UserDTO {
    private String userId;
    private String username;

    // TODO 추후 User 엔티티 username 필드 추가하던지 닉네임만 남기던지 필요
    private UserDTO(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public static UserDTO from(User user) {
        return new UserDTO(user.getId(), user.getNickname());
    }
}
