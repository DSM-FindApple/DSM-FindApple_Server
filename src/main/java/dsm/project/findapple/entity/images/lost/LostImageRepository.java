package dsm.project.findapple.entity.images.lost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostImageRepository extends JpaRepository<LostImage, Long> {
}
