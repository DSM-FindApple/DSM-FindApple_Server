package dsm.project.findapple.controller;

import dsm.project.findapple.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{imageName}")
    public byte[] getImage(@PathVariable String imageName) {
        return imageService.readImage(imageName);
    }
}
