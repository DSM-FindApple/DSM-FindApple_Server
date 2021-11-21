package dsm.project.findapple.service.chat;

import dsm.project.findapple.payload.response.BanUserResponse;
import dsm.project.findapple.payload.response.ChatRoomResponse;
import dsm.project.findapple.payload.response.JoinRoomResponse;

import java.util.List;

public interface ChatService {
    void banUser(String token, Long kakaoId);
    List<BanUserResponse> getMyBanList(String token, int pageNum);
    void cancelBan(String token, Long kakaoId);
    JoinRoomResponse joinRoom(String token, Long kakaoId);
    List<ChatRoomResponse> getMyChatRoom(String token, int pageNum);
}
