package dsm.project.findapple.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoIdAndKakaoNickName(Long kakaoId, String kakaoNickName);
    Optional<User> findByKakaoId(Long kakaoId);
}
