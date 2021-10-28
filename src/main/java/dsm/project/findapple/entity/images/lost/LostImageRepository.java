package dsm.project.findapple.entity.images.lost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LostImageRepository extends JpaRepository<LostImage, Long> {
    void deleteAllByLost_LostId(Long lost_lostId);

    @Query("select li.lostImageName from LostImage li")
    List<String> getImageNames(Long lostId);
}
