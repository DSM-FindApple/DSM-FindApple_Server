package dsm.project.findapple.service.message;

import dsm.project.findapple.payload.response.MessageResponse;
import dsm.project.findapple.payload.response.SendImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageService {
    List<MessageResponse> getMessages(String token, String chatId, int pageNum);
    SendImageResponse sendImage(String token, String chatId, MultipartFile image);
    void deleteMessage(String token, Long messageId);
}
