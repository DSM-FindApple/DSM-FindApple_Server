package dsm.project.findapple.payload.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AreaRequest {
    private Double startLongitude;
    private Double startLatitude;
    private Double endLongitude;
    private Double endLatitude;
}
