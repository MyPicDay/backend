package mypicday.store.user.dto.response;

import lombok.*;
import mypicday.store.user.entity.User;

import java.util.Objects;

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
    private String nickname;
    private String profileImageUrl;
    private boolean isFollowing;

    private UserSearchResponse(String nickname, String profileImageUrl, boolean isFollowing) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.isFollowing = isFollowing;
    }

    public static UserSearchResponse from(User user) {
        return new UserSearchResponse(
                user.getNickname(),
                // TODO userProfileImage, isFollowing 하드 코딩
                "",
                false
        );
    }
}
