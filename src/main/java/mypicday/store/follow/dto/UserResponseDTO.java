package mypicday.store.follow.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String userId;
    private String nickname;
    private String avatar;
    private String email;
    private long diaryCount;
    private long followerCount;
    private long followingCount;
    private boolean following;
    
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
