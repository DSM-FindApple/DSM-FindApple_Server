package dsm.project.findapple.payload.response;

import dsm.project.findapple.payload.enums.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Setter
public class LostResponse {
    private Long lostId;
    private String title;
    private Category category;
    private String detail;
    private Double longitude;
    private Double latitude;
    private LocalDate writeAt;
    private LocalDateTime lostAt;
    private Long kakaoId;
    private String lostUser;
    private TopCommentResponse topComment;
    private List<String> lostImages;
}
