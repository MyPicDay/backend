package mypicday.store.follow.controller;

import mypicday.store.follow.dto.UserProfileDTO;
import mypicday.store.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public class FollowingController {
    private FollowService followService;

    // 특정 사용자가 팔로우 중인 사용자 목록 조회
    @GetMapping("/{userId}/followingusers")
    public ResponseEntity<List<UserProfileDTO>> getFollowings(
            @PathVariable String userId,
            @RequestHeader("X-USER-ID") String loginUserId) {
        List<UserProfileDTO> followings = followService.getFollowings(userId, loginUserId);
        return ResponseEntity.ok(followings);
    }
}
