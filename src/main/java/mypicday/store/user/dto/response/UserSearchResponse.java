package mypicday.store.user.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mypicday.store.user.entity.User;

/**
 * @author : User
 * @packageName : mypicday.store.user.dto.response
 * @fileName : UserSearchResponse
 * @description :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 20.        User       최초 생성
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSearchResponse {
    private String userId;
    private String nickname;
    private String profileImageUrl;
    private boolean isFollowing;

    private UserSearchResponse(String userId, String nickname, String profileImageUrl, boolean isFollowing) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isFollowing = isFollowing;
    }

    public static UserSearchResponse from(User user) {
        return new UserSearchResponse(
                user.getId(),
                user.getNickname(),
                // TODO userProfileImage, isFollowing 하드 코딩
                "",
                false
        );
    }
}
