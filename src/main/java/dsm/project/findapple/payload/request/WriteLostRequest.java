package dsm.project.findapple.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dsm.project.findapple.payload.enums.Category;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class WriteLostRequest {
    private String title;
    private String detail;
    private Category category;
    private Double longitude;
    private Double latitude;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lostAt;
    private List<MultipartFile> images;
}
