package dsm.project.findapple.entity.images.find;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FindImageRepository extends JpaRepository<FindImage, Long> {
}
