package dsm.project.findapple.controller;

import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.request.UpdateLostRequest;
import dsm.project.findapple.payload.request.WriteLostRequest;
import dsm.project.findapple.payload.response.LostResponse;
import dsm.project.findapple.service.lost.LostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/lost")
@RequiredArgsConstructor
public class LostController {

    private final LostService lostService;

    @GetMapping("/{pageNum}")
    public List<LostResponse> getLost(@PathVariable int pageNum,
                                      @RequestHeader("Authorization") String token) {
        return lostService.readLost(token, pageNum);
    }

    @GetMapping("/search/title/{pageNum}")
    public List<LostResponse> searchByTitle(@PathVariable int pageNum,
                                            @RequestParam String title,
                                            @RequestHeader("Authorization") String token) {
        return lostService.searchLostByTitle(token, title, pageNum);
    }

    @GetMapping("/search/category/{pageNum}")
    public List<LostResponse> searchByCategory(@RequestHeader("Authorization") String token,
                                               @RequestParam Category category,
                                               @PathVariable int pageNum) {
        return lostService.searchLostByCategory(token, category, pageNum);
    }

    @GetMapping("/relation/{pageNum}")
    public List<LostResponse> getRelation(@PathVariable int pageNum,
                                          @RequestParam String title,
                                          @RequestHeader("Authorization") String token) {
        return lostService.readRelationLost(token, title, pageNum);
    }

    @PostMapping
    public void writeLost(@RequestHeader("Authorization") String token,
                          @RequestParam String title,
                          @RequestParam String detail,
                          @RequestParam Category category,
                          @RequestParam Double longitude,
                          @RequestParam Double latitude,
                          @RequestParam LocalDateTime lostAt,
                          @RequestParam List<MultipartFile> images) {
        lostService.writeLost(token,
                    WriteLostRequest.builder()
                            .category(category)
                            .detail(detail)
                            .images(images)
                            .latitude(latitude)
                            .longitude(longitude)
                            .lostAt(lostAt)
                            .title(title)
                            .build()
                );
    }

    @PutMapping("/{listId}")
    public void updateLost(@RequestHeader("Authorization") String token,
                           @PathVariable Long listId,
                           @RequestBody UpdateLostRequest updateLostRequest) {
        lostService.updateLost(token, listId, updateLostRequest);
    }

    @PutMapping("/image/{lostId}")
    public void updateLostImage(@RequestHeader("Authorization") String token,
                                @PathVariable Long lostId,
                                @RequestParam List<MultipartFile> images) {
        lostService.updateLostImage(token, lostId, images);
    }

    @DeleteMapping("/{lostId}")
    public void deleteLost(@PathVariable Long lostId,
                           @RequestHeader("Authorization") String token) {
        lostService.deleteLost(token, lostId);
    }
}
