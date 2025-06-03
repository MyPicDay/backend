package mypicday.store.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUserInfoDTO {
		private String userId;
    private int diaryCount;
    private int followerCount;
    private int followingCount;
    private String nickname;
    private String avatar;
    private String email;
}
