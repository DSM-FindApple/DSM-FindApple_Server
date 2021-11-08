package dsm.project.findapple.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dsm.project.findapple.payload.enums.Category;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateLostRequest {
    private String title;
    private String detail;
    private Double longitude;
    private Double latitude;
    private Category category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lostAt;
}
