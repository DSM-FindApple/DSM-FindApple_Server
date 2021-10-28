package dsm.project.findapple.entity.recomment.repository;

import dsm.project.findapple.entity.recomment.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommentRepository extends JpaRepository<Recomment, Long> {
}
