package dsm.project.findapple.service.comment;

import dsm.project.findapple.payload.enums.CommentType;
import dsm.project.findapple.payload.request.UpdateCommentRequest;
import dsm.project.findapple.payload.request.WriteCommentRequest;
import dsm.project.findapple.payload.response.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> readComment(String token, Long id, CommentType commentType);
    void writeComment(String token, Long id, CommentType commentType, WriteCommentRequest writeCommentRequest);
    void writeReComment(String token, Long commentId, WriteCommentRequest writeCommentRequest);
    void updateComment(String token, Long commentId, UpdateCommentRequest updateCommentRequest);
    void updateReComment(String token, Long commentId, UpdateCommentRequest updateCommentRequest);
    void deleteComment(String token, Long commentId);
    void deleteReComment(String token, Long reCommentId);
}
