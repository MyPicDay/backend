package mypicday.store.global.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mypicday.store.auth.jwt.JwtProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
// NOTE: 임시로 SseAuthenticationFilter를 사용하여 SSE 요청에 대한 인증을 처리합니다.
public class SseAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 Accept 가져오기
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        // 2. SSE(text/event-stream)인지 확인
        if (acceptHeader != null && acceptHeader.contains("text/event-stream")) {

            // 3. Authorization 헤더에서 JWT 토큰 추출
            String token = extractTokenForSse(request);

            // 4. 토큰이 없거나 유효하지 않은 경우 401 Unauthorized 반환
            if (token == null || !jwtProvider.validateToken(token)) {
                log.info("토큰이 없거나 유효하지 않습니다. 요청 URI: {}", request.getRequestURI());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // 5. 토큰이 유효한 경우, Authentication을 생성해서 SecurityContext에 저장해서 인증 세팅
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 6. 다음에 실행될 Spring Security 필터로 요청 넘김
        filterChain.doFilter(request, response);
    }


    // Authorization 헤더에서 토큰만 추출하는 메서드
    private String extractTokenForSse(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후 토큰 부분만 추출
        }
        return null;
    }
}