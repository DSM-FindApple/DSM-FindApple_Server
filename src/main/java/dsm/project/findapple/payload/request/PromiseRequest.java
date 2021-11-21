package dsm.project.findapple.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PromiseRequest {
    private String script;
    private Double longitude;
    private Double latitude;
    private Long targetId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetAt;
}
