package dsm.project.findapple.entity.comment.repository;

import dsm.project.findapple.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByLost_LostIdOrderByWriteAtDesc(Long lost_lostId);
}
