package mypicday.store.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import mypicday.store.user.entity.User;

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
    
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getNickname(); 
        this.avatar = user.getAvatar();
        this.email = user.getEmail();
    }
