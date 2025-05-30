package mypicday.store.follow.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.follow.dto.UserResponseDTO;
import mypicday.store.follow.entity.Follow;
import mypicday.store.follow.repository.FollowRepository;
import mypicday.store.follow.service.FollowService;
import mypicday.store.user.dto.response.UserSearchResponse;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users/{userId}")
@RequiredArgsConstructor
public class FollowAPIController {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;



    @GetMapping
    public UserResponseDTO getUserSummary(@PathVariable String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        long followers = followRepository.countByFollowing_Id(userId);
        long followings = followRepository.countByFollower_Id(userId);

        return new UserResponseDTO(
                user.getId(),
                user.getNickname(),
                user.getAvatar(),
                followers,
                followings
        );
    }
}
