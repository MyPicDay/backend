package mypicday.store.follow.controller;

import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import mypicday.store.follow.dto.UserProfileDTO;
import mypicday.store.follow.dto.UserResponseDTO;
import mypicday.store.follow.entity.Follow;
import mypicday.store.follow.repository.FollowRepository;
import mypicday.store.follow.service.FollowService;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.user.entity.User;
import mypicday.store.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowerControllerDetail {

    private final FollowService followService;

    @GetMapping("/{userId}/followings")
    public ResponseEntity<List<UserProfileDTO>> getFollowers(@PathVariable String userId) {
        List<UserProfileDTO> result = followService.getFollowers(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{targetUserId}/follow")
    public ResponseEntity<Void> follow(@PathVariable String targetUserId,
                                       @AuthenticationPrincipal UserDetails user) {
        followService.follow(user.getUsername(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUserId}/follow")
    public ResponseEntity<Void> unfollow(@PathVariable String targetUserId,
                                         @AuthenticationPrincipal UserDetails user) {
        followService.unfollow(user.getUsername(), targetUserId);
        return ResponseEntity.ok().build();
    }
}
