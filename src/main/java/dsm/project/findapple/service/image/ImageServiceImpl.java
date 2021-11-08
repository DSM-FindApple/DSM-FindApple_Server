package dsm.project.findapple.service.image;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${image.dir}")
    private String fileDir;

    @Override
    @SneakyThrows
    public void uploadImage(MultipartFile image, String imageName) {
        image.transferTo(new File(fileDir, imageName));
    }

    @SneakyThrows
    @Override
    public void delete(String imageName) {
        File file = new File(fileDir, imageName);
        if(!file.exists())
            throw new FileNotFoundException();

        file.delete();
    }

    @SneakyThrows
    @Override
    public byte[] readImage(String imageName) {
        File file = new File(fileDir, imageName);

        if(!file.exists())
            throw new FileNotFoundException();

        return IOUtils.toByteArray(new FileInputStream(file));
    }
}
