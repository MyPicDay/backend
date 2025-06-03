package mypicday.store.follow.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import mypicday.store.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String id;
    private String userId;
    private String name;
    private String nickname;
    private String avatar;
    private String email;
    private long diaryCount;
    private long followerCount;
    private long followingCount;
    private boolean following;
    private long followersCount;
    private long followingsCount;

    public UserResponseDTO(User user) {
        private String email;
        public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getNickname();
        this.avatar = user.getAvatar();
    }
    private long diaryCount;
    private long followerCount;
    private long followingCount;
    private boolean following;
}
