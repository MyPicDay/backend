package mypicday.store.follow.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.follow.dto.UserResponseDTO;
import mypicday.store.follow.entity.Follow;
import mypicday.store.follow.repository.FollowRepository;
import mypicday.store.user.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowerControllerDetail {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    // 내가 팔로우한 사람들
    @GetMapping("/{userId}/followings")
    public List<UserResponseDTO> getFollowings(@PathVariable String userId) {
        List<Follow> followings = followRepository.findByFollower_Id(userId);
        return followings.stream()
                .map(f -> new UserResponseDTO(f.getFollowing()))
                .collect(Collectors.toList());
    }

    // 나를 팔로우한 사람들
    @GetMapping("/{userId}/followers")
    public List<UserResponseDTO> getFollowers(@PathVariable String userId) {
        List<Follow> followers = followRepository.findByFollowing_Id(userId);
        return followers.stream()
                .map(f -> new UserResponseDTO(f.getFollower()))
                .collect(Collectors.toList());
    }
}
