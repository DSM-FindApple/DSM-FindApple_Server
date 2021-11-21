package dsm.project.findapple.entity.deviceToken;

import dsm.project.findapple.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    boolean existsByUserAndDeviceToken(User user, String deviceToken);
    Optional<DeviceToken> findByDeviceTokenAndUser(String deviceToken, User user);
    void deleteByDeviceTokenAndUser(String deviceToken, User user);

    @Query("select d.deviceToken from DeviceToken d where d.user = ?1")
    List<String> getUserDeviceToken(User user);
}
