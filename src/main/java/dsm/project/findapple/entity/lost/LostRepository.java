package dsm.project.findapple.entity.lost;

import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.payload.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {
    Optional<Lost> findAllByLostId(Long lostId);
    Page<Lost> findAllByUser(User user, Pageable pageable);
    Page<Lost> findAllByCategory(Category category, Pageable pageable);
    Page<Lost> findAllByTitleContaining(String title, Pageable pageable);
    void deleteByLostId(Long lostId);
    Page<Lost> findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(Double area_longitude, Double area_longitude2, Double area_latitude, Double area_latitude2, Pageable pageable);
    List<Lost> findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(Double area_longitude, Double area_longitude2, Double area_latitude, Double area_latitude2);
    Page<Lost> findAll(Pageable pageable);
    int countAllByUser(User user);
    List<Lost> findAllByUser(User user);
}
