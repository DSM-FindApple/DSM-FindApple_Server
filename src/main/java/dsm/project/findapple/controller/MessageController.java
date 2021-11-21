package dsm.project.findapple.controller;

import dsm.project.findapple.payload.response.MessageResponse;
import dsm.project.findapple.payload.response.SendImageResponse;
import dsm.project.findapple.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/{chatId}/{pageNum}")
    public List<MessageResponse> getMessages(@PathVariable String chatId,
                                             @PathVariable int pageNum,
                                             @RequestHeader("Authorization") String token) {
        return messageService.getMessages(token, chatId, pageNum);
    }

    @PostMapping("/{chatId}")
    public SendImageResponse sendImage(@RequestHeader("Authorization") String token,
                                       @RequestParam MultipartFile image,
                                       @PathVariable String chatId) {
        return messageService.sendImage(token, chatId, image);
    }

    @DeleteMapping("/{messageId}")
    public void deleteMessage(@PathVariable Long messageId,
                              @RequestHeader("Authorization") String token) {
        messageService.deleteMessage(token, messageId);
    }
}
