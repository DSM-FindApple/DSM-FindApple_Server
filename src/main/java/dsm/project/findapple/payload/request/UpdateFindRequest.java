package dsm.project.findapple.payload.request;

import dsm.project.findapple.payload.enums.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateFindRequest {
    private String title;
    private String detail;
    private Double longitude;
    private Double latitude;
    private Category category;
    private LocalDateTime lostAt;
}
