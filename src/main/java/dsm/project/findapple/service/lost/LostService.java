package dsm.project.findapple.service.lost;

import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.request.AreaRequest;
import dsm.project.findapple.payload.request.UpdateLostRequest;
import dsm.project.findapple.payload.request.WriteLostRequest;
import dsm.project.findapple.payload.response.FindResponse;
import dsm.project.findapple.payload.response.LostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LostService {
    void writeLost(String token, WriteLostRequest writeLostRequest);
    void updateLost(String token, Long lostId, UpdateLostRequest updateLostRequest);
    void updateLostImage(String token, Long lostId, List<MultipartFile> lostImages);
    List<LostResponse> readLost(String token, int pageNum, AreaRequest areaRequest);
    List<LostResponse> searchLostByTitle(String token, String title, int pageNum);
    List<LostResponse> searchLostByCategory(String token, Category category, int pageNum);
    List<FindResponse> readRelationLost(String token, String title, int pageNum);
    List<LostResponse> getMyLost(String token, int pageNum);
    void deleteLost(String token, Long lostId);
}
