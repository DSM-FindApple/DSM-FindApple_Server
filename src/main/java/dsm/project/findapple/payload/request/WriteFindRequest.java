package dsm.project.findapple.payload.request;

import dsm.project.findapple.payload.enums.Category;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class WriteFindRequest {
    private String title;
    private String detail;
    private Category category;
    private Double longitude;
    private Double latitude;
    private LocalDateTime findAt;
    private List<MultipartFile> images;
}
