package dsm.project.findapple.entity.refresh_token;

import dsm.project.findapple.entity.refresh_token.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    void deleteByKakaoId(Long kakaoId);
}
