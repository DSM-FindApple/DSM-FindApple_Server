package dsm.project.findapple.entity.comment.repository;

import dsm.project.findapple.entity.comment.Comment;
import dsm.project.findapple.entity.find.Find;
import dsm.project.findapple.entity.lost.Lost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findTop1ByLost_LostIdOrderByWriteAtDesc(Long lost_lostId);
    Optional<Comment> findTop1ByFind_FindIdOrderByWriteAtDesc(Long find_findId);
    List<Comment> findAllByFind(Find find);
    List<Comment> findAllByLost(Lost lost);
    Optional<Comment> findByCommentId(Long commentId);
    void deleteByCommentId(Long commentId);
}
