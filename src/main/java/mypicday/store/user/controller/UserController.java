package mypicday.store.user.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.follow.dto.UserResponseDTO;
import mypicday.store.global.config.CustomUserDetails;
import mypicday.store.user.entity.User;
import mypicday.store.user.entity.UserResponse;
import mypicday.store.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userId = userDetails.getId();

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        UserResponse response = new UserResponse(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getAvatar() // or full URL path 처리
        );

        return ResponseEntity.ok(response);
    }
}