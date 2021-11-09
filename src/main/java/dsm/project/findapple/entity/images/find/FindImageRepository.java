package dsm.project.findapple.entity.images.find;

import dsm.project.findapple.entity.find.Find;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FindImageRepository extends JpaRepository<FindImage, Long> {
    void deleteAllByFind(Find find);

    @Query("select li.imageName from FindImage li")
    List<String> getImageNames(Long lostId);
}
