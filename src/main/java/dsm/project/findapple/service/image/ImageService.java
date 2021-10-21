package dsm.project.findapple.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void uploadImage(MultipartFile image, String imageName);
    void delete(String imageName);
    byte[] readImage(String imageName);
}
