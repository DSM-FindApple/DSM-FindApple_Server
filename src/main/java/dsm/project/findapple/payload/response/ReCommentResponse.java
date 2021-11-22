package dsm.project.findapple.payload.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReCommentResponse {
    private Long recommentId;
    private Long commentId;
    private Long userId;
    private String nickName;
    private String comment;
    private String profileUrl;
    private LocalDateTime writeAt;
}
