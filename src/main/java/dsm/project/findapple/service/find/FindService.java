package dsm.project.findapple.service.find;

import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.request.AreaRequest;
import dsm.project.findapple.payload.request.UpdateFindRequest;
import dsm.project.findapple.payload.request.WriteFindRequest;
import dsm.project.findapple.payload.response.FindResponse;
import dsm.project.findapple.payload.response.LostResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FindService {
    void writeFind(String token, WriteFindRequest writeFindRequest);
    void updateFind(String token, Long findId, UpdateFindRequest updateFindRequest);
    void updateFindImage(String token, Long findId, List<MultipartFile> findImages);
    List<FindResponse> getFindByArea(String token, AreaRequest areaRequest);
    List<FindResponse> readFind(String token, int pageNum, AreaRequest areaRequest);
    List<FindResponse> searchFindByTitle(String token, String title, int pageNum);
    List<FindResponse> searchFindByCategory(String token, Category category, int pageNum);
    List<LostResponse> readRelationFind(String token, String title, int pageNum);
    List<FindResponse> getMyFind(String token, int pageNum);
    void deleteFind(String token, Long findId);
}
