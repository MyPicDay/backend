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
}
