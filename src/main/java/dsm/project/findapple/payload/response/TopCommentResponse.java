package dsm.project.findapple.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TopCommentResponse {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String nickName;
    private String comment;
    private LocalDateTime writeAt;
}
