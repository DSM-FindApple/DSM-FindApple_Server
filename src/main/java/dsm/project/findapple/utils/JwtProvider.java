package dsm.project.findapple.utils;

import dsm.project.findapple.error.exceptions.InvalidTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.access}")
    private Integer expiredAccess;

    @Value("${jwt.refresh}")
    private Integer expiredRefresh;

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(Long kakaoId) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredAccess * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .claim("type", "access_token")
                .setSubject(kakaoId.toString())
                .compact();
    }

    public String generateRefreshToken(Long kakaoId) {
        log.info(expiredRefresh.toString());
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredRefresh * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .claim("type", "refresh_token")
                .setSubject(kakaoId.toString())
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody().getSubject();

            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                .getBody().get("type").equals("refresh_token");
    }

    public Long getKakaoId(String token) {
        if(!validateToken(token))
            throw new InvalidTokenException();

        return Long.parseLong(Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody().getSubject());
    }
}
