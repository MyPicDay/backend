package mypicday.store.follow.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.follow.dto.UserResponseDTO;
import mypicday.store.follow.entity.FollowId;
import mypicday.store.follow.repository.FollowRepository;
import mypicday.store.follow.service.FollowService;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getProfile(@AuthenticationPrincipal CustomUserDetails currentUser, @PathVariable String userId) {
        User target = userRepository.findById(userId).orElseThrow();

        boolean isFollowing = followRepository.existsById(new FollowId(target.getId(), currentUser.getId()));

        long diaryCount = diaryRepository.countByUserId(target.getId());
        long followerCount = followRepository.countByFollower_Id(target.getId());
        long followingCount = followRepository.countByFollowing_Id(target.getId());

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .userId(target.getId())
                .nickname(target.getNickname())
                .email(target.getEmail())
                .avatar(target.getAvatar())
                .diaryCount(diaryCount)
                .followerCount(followerCount)
                .followingCount(followingCount)
                .following(isFollowing) // 핵심: 로그인한 유저 기준으로 팔로우 여부 반환
                .build();
        return ResponseEntity.ok(userResponseDTO);
    }


    //팔로우
    @PostMapping("/{userId}")
    //팔로우 받는 사람 userId를 가져오고 JWT 인증을 통해 로그인 한 내 정보를 가져옴
    public ResponseEntity<Void> follow(@AuthenticationPrincipal CustomUserDetails user, @PathVariable String userId) {
        //팔로우서비스에서 팔로우
        followService.follow(user.getId(), userId);
        //200 반환
         return ResponseEntity.ok().build();
    }

    //언팔로우
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> unfollow(@PathVariable String userId, @AuthenticationPrincipal CustomUserDetails me){
        //언팔로우 유저 조회
        User target = userRepository.findById(userId).orElseThrow();
        //복합키를 이용하여 target와 me 사이의 Follow 관계를 삭제
        followRepository.deleteById(new FollowId(target.getId(), me.getId()));
        //200
        return ResponseEntity.ok().build();
    }
}
