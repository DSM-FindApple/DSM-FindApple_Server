package dsm.project.findapple.utils;

import dsm.project.findapple.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ValidateImage {

    private final ImageService s3Service;

    public String validateImage(MultipartFile image) {
        String origin = image.getOriginalFilename();

        if(origin == null || origin.isEmpty())
            throw new RuntimeException("Image name bad request");

        String ex = StringUtils.getFilenameExtension(origin);
        if(ex == null || ex.isEmpty())
            throw new RuntimeException("Ex bad request");

        if(!(ex.contains("jpg") || ex.contains("jpeg") || ex.contains("png"))) {
            throw new RuntimeException("ex bad request");
        }

        String fileName = UUID.randomUUID() + "." + ex;

        s3Service.uploadImage(image, fileName);

        return fileName;
    }
}
