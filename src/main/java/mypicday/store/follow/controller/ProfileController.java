package mypicday.store.follow.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.follow.dto.UserResponseDTO;
import mypicday.store.follow.repository.FollowRepository;
import mypicday.store.diary.repository.DiaryRepository;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final FollowRepository followRepository;
    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public UserResponseDTO getUserSummary(@PathVariable String userId, @AuthenticationPrincipal CustomUserDetails me) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        long followers = followRepository.countByFollowing_Id(userId);
        long followings = followRepository.countByFollower_Id(userId);
        long diaryCount = diaryRepository.countByUser_Id(userId);

        boolean isFollowing = followRepository.existsByFollower_IdAndFollowing_Id(me.getId(), userId);

        return new UserResponseDTO(
                user.getId(),
                user.getNickname(),
                user.getAvatar(),
                user.getEmail(),
                diaryCount,
                followings,
                followers,
                isFollowing
        );
    }
}