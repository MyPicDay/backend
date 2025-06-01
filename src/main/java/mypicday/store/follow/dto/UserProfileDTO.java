package mypicday.store.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserProfileDTO {
    private String id;
    private String nickname;
    private String avatar;
    private String email;
    private boolean isFollowing;

}
