package dsm.project.findapple.controller;

import dsm.project.findapple.payload.enums.CommentType;
import dsm.project.findapple.payload.request.UpdateCommentRequest;
import dsm.project.findapple.payload.request.WriteCommentRequest;
import dsm.project.findapple.payload.response.CommentResponse;
import dsm.project.findapple.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{commentType}/{id}")
    public List<CommentResponse> getComments(@PathVariable Long id,
                                             @PathVariable CommentType commentType,
                                             @RequestHeader("Authorization") String token) {
        return commentService.readComment(token, id, commentType);
    }

    @PostMapping("/{commentType}/{id}")
    public void writeComment(@PathVariable Long id,
                             @PathVariable CommentType commentType,
                             @RequestHeader("Authorization") String token,
                             @RequestBody WriteCommentRequest writeCommentRequest) {
        commentService.writeComment(token, id, commentType, writeCommentRequest);
    }

    @PostMapping("/recomment/{commentId}")
    public void writeRecomment(@PathVariable Long commentId,
                               @RequestHeader("Authorization") String token,
                               @RequestBody WriteCommentRequest writeCommentRequest) {
        commentService.writeReComment(token, commentId, writeCommentRequest);
    }

    @PutMapping("/{commentId}")
    public void updateComment(@PathVariable Long commentId,
                              @RequestHeader("Authorization") String token,
                              @RequestBody UpdateCommentRequest updateCommentRequest) {
        commentService.updateComment(token, commentId, updateCommentRequest);
    }

    @PutMapping("/recomment/{recommentId}")
    public void updateRecomment(@PathVariable Long recommentId,
                                @RequestHeader("Authorization") String token,
                                @RequestBody UpdateCommentRequest updateCommentRequest) {
        commentService.updateReComment(token, recommentId, updateCommentRequest);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long commentId,
                              @RequestHeader("Authorization") String token) {
        commentService.deleteComment(token, commentId);
    }

    @DeleteMapping("/recomment/{recommentId}")
    public void deleteRecomment(@PathVariable Long recommentId,
                                @RequestHeader("Authorization") String token) {
        commentService.deleteReComment(token, recommentId);
    }
}
