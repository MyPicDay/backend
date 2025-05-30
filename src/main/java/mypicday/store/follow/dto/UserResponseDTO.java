package mypicday.store.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import mypicday.store.user.entity.User;

@Data
@AllArgsConstructor
public class UserResponseDTO {
    private String id;
    private String name;
    private String avatar;
    private long followersCount;
    private long followingsCount;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getNickname();
        this.avatar = user.getAvatar();
    }
}
