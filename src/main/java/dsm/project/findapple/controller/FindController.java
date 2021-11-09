package dsm.project.findapple.controller;

import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.request.AreaRequest;
import dsm.project.findapple.payload.request.UpdateFindRequest;
import dsm.project.findapple.payload.request.WriteFindRequest;
import dsm.project.findapple.payload.response.FindResponse;
import dsm.project.findapple.payload.response.LostResponse;
import dsm.project.findapple.service.find.FindService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/find")
@RequiredArgsConstructor
public class FindController {

    private final FindService findService;

    @GetMapping("/{pageNum}")
    public List<FindResponse> getFindByArea(@PathVariable int pageNum,
                                            @RequestParam Double startLongitude,
                                            @RequestParam Double startLatitude,
                                            @RequestParam Double endLongitude,
                                            @RequestParam Double endLatitude,
                                            @RequestHeader("Authorization") String token) {
        return findService.readFind(
                token,
                pageNum,
                AreaRequest.builder()
                        .endLatitude(endLatitude)
                        .endLongitude(endLongitude)
                        .startLatitude(startLatitude)
                        .startLongitude(startLongitude)
                        .build()
        );
    }

    @GetMapping("/search/title/{pageNum}")
    public List<FindResponse> searchFindByTitle(@PathVariable int pageNum,
                                                @RequestParam String title,
                                                @RequestHeader("Authorization") String token) {
        return findService.searchFindByTitle(token, title, pageNum);
    }

    @GetMapping("/search/category/{pageNum}")
    public List<FindResponse> searchFindByCategory(@PathVariable int pageNum,
                                                   @RequestParam Category category,
                                                   @RequestHeader("Authorization") String token) {
        return findService.searchFindByCategory(token, category, pageNum);
    }

    @GetMapping("/relation/{pageNum}")
    public List<LostResponse> getFindByRelation(@PathVariable int pageNum,
                                                @RequestParam String title,
                                                @RequestHeader("Authorization") String token) {
        return findService.readRelationFind(token, title, pageNum);
    }

    @GetMapping("/me/{pageNum}")
    public List<FindResponse> getMyFind(@PathVariable int pageNum,
                                        @RequestHeader("Authorization") String token) {
        return findService.getMyFind(token, pageNum);
    }

    @PostMapping
    public void writeFind(@RequestHeader("Authorization") String token,
                          @RequestParam String title,
                          @RequestParam String detail,
                          @RequestParam Category category,
                          @RequestParam Double longitude,
                          @RequestParam Double latitude,
                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") LocalDateTime findAt,
                          @RequestParam List<MultipartFile> images) {
        findService.writeFind(
                token,
                WriteFindRequest.builder()
                        .category(category)
                        .detail(detail)
                        .findAt(findAt)
                        .images(images)
                        .latitude(latitude)
                        .longitude(longitude)
                        .title(title)
                        .build()
        );
    }

    @PutMapping("/{findId}")
    public void updateFind(@RequestHeader("Authorization") String token,
                           @PathVariable Long findId,
                           @RequestBody UpdateFindRequest updateFindRequest) {
        findService.updateFind(token, findId, updateFindRequest);
    }

    @PutMapping("/image/{findId}")
    public void updateFindImage(@RequestHeader("Authorization") String token,
                                @PathVariable Long findId,
                                @RequestParam List<MultipartFile> images) {
        findService.updateFindImage(token, findId, images);
    }

    @DeleteMapping("/{findId}")
    public void deleteFind(@RequestHeader("Authorization") String token,
                           @PathVariable Long findId) {
        findService.deleteFind(token, findId);
    }
}
