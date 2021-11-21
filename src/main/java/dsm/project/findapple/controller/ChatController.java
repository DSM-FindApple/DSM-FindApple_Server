package dsm.project.findapple.controller;

import dsm.project.findapple.payload.response.BanUserResponse;
import dsm.project.findapple.payload.response.ChatRoomResponse;
import dsm.project.findapple.payload.response.JoinRoomResponse;
import dsm.project.findapple.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{pageNum}")
    public List<ChatRoomResponse> getMyChatRoom(@RequestHeader("Authorization") String token,
                                                @PathVariable int pageNum) {
        return chatService.getMyChatRoom(token, pageNum);
    }

    @GetMapping("/ban/{pageNum}")
    public List<BanUserResponse> getMyBanList(@RequestHeader("Authorization") String token,
                                              @PathVariable int pageNum) {
        return chatService.getMyBanList(token, pageNum);
    }

    @PostMapping("/{kakaoId}")
    public JoinRoomResponse joinRoom(@RequestHeader("Authorization") String token,
                                     @PathVariable Long kakaoId) {
        return chatService.joinRoom(token, kakaoId);
    }

    @PostMapping("/ban/{kakaoId}")
    public void banUser(@RequestHeader("Authorization") String token,
                        @PathVariable Long kakaoId) {
        chatService.banUser(token, kakaoId);
    }

    @DeleteMapping("/ban/{kakaoId}")
    public void cancelBan(@RequestHeader("Authorization") String token,
                          @PathVariable Long kakaoId) {
        chatService.cancelBan(token, kakaoId);
    }
}
