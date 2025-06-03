package mypicday.store.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import mypicday.store.user.entity.User;

@Data
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

    // 생성자 오버로딩: User 엔티티를 DTO로 변환
public UserResponseDTO(User user) {
    this.id = user.getId();
    this.userId = user.getId(); // 혹시 id와 userId를 다르게 쓸 게 아니라면 이대로 사용
    this.name = user.getNickname(); // name 필드는 nickname에서 가져옴
    this.nickname = user.getNickname();
    this.avatar = user.getAvatar();
    this.email = user.getEmail();
}
}
