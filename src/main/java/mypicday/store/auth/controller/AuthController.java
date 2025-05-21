package mypicday.store.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*
    * 회원가입
    * */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest dto) {
        log.info("[회원가입] 요청 : 이메일={}, 닉네임={}", dto.getEmail(), dto.getNickname());
        try {
            authService.signup(dto);
            log.info("[회원가입] 성공 : 이메일={}", dto.getEmail());
            return ResponseEntity.ok("회원가입 성공");
        } catch (RuntimeException e) {
            log.warn("[회원가입] 실패 : {}", e.getMessage());
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    /*
    * 로그인
    * */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        log.info("[로그인] 요청 수신: 이메일={}", dto.getEmail());

        try {
            String token = authService.login(dto);
            log.info("[로그인] 성공 : 이메일={}", dto.getEmail());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .body("로그인 성공");
        } catch (RuntimeException e) {
            log.warn("[로그인] 실패 : {}", e.getMessage());
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }
}

