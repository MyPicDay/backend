package mypicday.store.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private final long validityInMs = 3600000; // 1시간

    public String generateToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        claims.put("roles", List.of("ROLE_USER")); // 또는 user.getRoles()

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }

    public String getEmailFromToken(String token) {
        token = cleanToken(token); // "Bearer " 같은 prefix 제거 메서드

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)  // secretKey는 String 또는 SecretKey 객체
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // JWT의 subject가 email
    }

    public boolean validateToken(String token) {
        try {
            token = cleanToken(token);
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
//            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }


    // Bearer 제거
    private String cleanToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }

}

