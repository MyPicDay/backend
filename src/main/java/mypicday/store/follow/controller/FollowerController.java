package mypicday.store.follow.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.follow.dto.UserProfileDTO;
import mypicday.store.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class FollowerController {

    private final FollowService followService;

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<UserProfileDTO>> getFollowers(@PathVariable String userId) {
        List<UserProfileDTO> result = followService.getFollowers(userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{targetUserId}/follow") //팔로우 요청 버튼
    public ResponseEntity<Void> follow(@PathVariable String targetUserId,
                                       @AuthenticationPrincipal UserDetails user) {
        followService.follow(user.getUsername(), targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUserId}/follow")//팔로우 취소 요청 버튼
    public ResponseEntity<Void> unfollow(@PathVariable String targetUserId,
                                         @AuthenticationPrincipal UserDetails user) {
        followService.unfollow(user.getUsername(), targetUserId);
        return ResponseEntity.ok().build();
    }
}
