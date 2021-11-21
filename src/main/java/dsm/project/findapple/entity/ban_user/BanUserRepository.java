package dsm.project.findapple.entity.ban_user;

import dsm.project.findapple.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BanUserRepository extends JpaRepository<BanUser, Long> {
    Optional<BanUser> findByUserAndBanUser(User user, User banUser);
    Page<BanUser> findAllByUser(User user, Pageable pageable);
    boolean existsByUserAndBanUser(User user, User banUser);
    void deleteByBanUserAndUser(User banUser, User user);
}
