package mypicday.store.diary.dto;

import lombok.Getter;

/**
 * @author : User
 * @packageName : mypicday.diary.dto
 * @fileName : UserDTO
 * @description :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 25. 5. 19.        User       최초 생성
 */
// TODO 임시 (삭제 예정)
@Getter
public class UserDTO {
    private long userId;
    private String username;
    private String profileImageUrl;

    public UserDTO(long userId, String username, String profileImageUrl) {
        this.userId = userId;
        this.username = username;
        this.profileImageUrl = profileImageUrl;
    }
}
