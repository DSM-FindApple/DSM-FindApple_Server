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
public class FindResponse {
    private Long findId;
    private String title;
    private Category category;
    private String detail;
    private Double longitude;
    private Double latitude;
    private LocalDate writeAt;
    private LocalDateTime findAt;
    private Long kakaoId;
    private String findUser;
    private TopCommentResponse topComment;
    private List<String> findImages;
}
