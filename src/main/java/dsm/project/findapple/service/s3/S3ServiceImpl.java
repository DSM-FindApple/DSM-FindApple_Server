package dsm.project.findapple.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service{

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    @SneakyThrows
    public void uploadImage(MultipartFile image, String imageName) {
        amazonS3.putObject(
                new PutObjectRequest(bucket, imageName, image.getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    @Override
    public void delete(String imageName) {
        amazonS3.deleteObject(bucket, imageName);
    }

    @SneakyThrows
    @Override
    public byte[] readImage(String imageName) {
        S3Object s3Object = amazonS3.getObject(bucket, imageName);
        S3ObjectInputStream stream = s3Object.getObjectContent();
        return IOUtils.toByteArray(stream);
    }
}
