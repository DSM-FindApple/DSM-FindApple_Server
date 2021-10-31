package dsm.project.findapple.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.access}")
    private Integer expiredAccess;

    @Value("${jwt.refresh}")
    private Integer expiredRefresh;

    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.secret}")
    private String secret;

    public String generateAccessToken(Long kakaoId) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredAccess * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .claim("type", "access_token")
                .setSubject(kakaoId.toString())
                .compact();
    }

    public String generateRefreshToken(String accessToken) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredAccess * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .claim("type", "refresh_token")
                .setSubject(accessToken)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token).getBody().getSubject();

            return false;
        }catch (Exception e) {
            return true;
        }
    }

    public boolean isRefreshToken(String token) {
        if(!validateToken(token))
            throw new RuntimeException("invalid token");

        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("type").equals("refresh_token");
    }

    public String getAccessToken(String token) {
        if(validateToken(token))
            throw new RuntimeException("invalid token");

        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody().getSubject();
    }

    public Long getKakaoId(String token) {
        if(validateToken(token))
            throw new RuntimeException("invalid token");

        return Long.getLong(Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody().getSubject());
    }
}
