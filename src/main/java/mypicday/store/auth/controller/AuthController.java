package mypicday.store.auth.controller;

import lombok.RequiredArgsConstructor;
import mypicday.store.auth.dto.LoginRequest;
import mypicday.store.auth.dto.SignupRequest;
import mypicday.store.auth.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest dto) {
        authService.signup(dto);
        return ResponseEntity.ok("회원가입 성공");

    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        String token = authService.login(dto);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body("로그인 성공");
    }
}

