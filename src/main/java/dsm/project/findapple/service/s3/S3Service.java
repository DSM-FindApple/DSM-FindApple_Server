package dsm.project.findapple.service.s3;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    void uploadImage(MultipartFile image, String imageName);
    void delete(String imageName);
    byte[] readImage(String imageName);
}
