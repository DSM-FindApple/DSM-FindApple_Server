package dsm.project.findapple.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponse {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String nickName;
    private String comment;
    private LocalDateTime writeAt;
    private List<ReCommentResponse> reComment;
}
