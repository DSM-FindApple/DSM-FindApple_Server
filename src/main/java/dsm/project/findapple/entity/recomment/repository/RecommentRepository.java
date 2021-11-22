package dsm.project.findapple.entity.recomment.repository;

import dsm.project.findapple.entity.comment.Comment;
import dsm.project.findapple.entity.recomment.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommentRepository extends JpaRepository<Recomment, Long> {
    List<Recomment> findAllByCommentId(Comment commentId);
    Optional<Recomment> findByRecommentId(Long recommentId);
    void deleteByRecommentId(Long recommentId);
    void deleteAllByCommentId(Comment commentId);
}
