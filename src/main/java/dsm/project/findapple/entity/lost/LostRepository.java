package dsm.project.findapple.entity.lost;

import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.payload.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LostRepository extends JpaRepository<Lost, Long> {
    Optional<Lost> findAllByLostId(Long lostId);
    Page<Lost> findAllByUser(User user, Pageable pageable);
    Page<Lost> findAllByCategory(Category category, Pageable pageable);
    Page<Lost> findAllByTitleContaining(String title, Pageable pageable);
    void deleteByLostId(Long lostId);
    Page<Lost> findAll(Pageable pageable);
}
