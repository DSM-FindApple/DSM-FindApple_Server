package dsm.project.findapple.service.chat;

import dsm.project.findapple.entity.ban_user.BanUser;
import dsm.project.findapple.entity.ban_user.BanUserRepository;
import dsm.project.findapple.entity.chat.Chat;
import dsm.project.findapple.entity.chat.ChatRepository;
import dsm.project.findapple.entity.chat_user.ChatUser;
import dsm.project.findapple.entity.chat_user.ChatUserRepository;
import dsm.project.findapple.entity.deviceToken.DeviceTokenRepository;
import dsm.project.findapple.entity.message.Message;
import dsm.project.findapple.entity.message.MessageRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.AlreadyBanUserException;
import dsm.project.findapple.error.exceptions.BanUserNotFoundException;
import dsm.project.findapple.error.exceptions.ChatUserNotFoundException;
import dsm.project.findapple.error.exceptions.UserNotFoundException;
import dsm.project.findapple.payload.response.BanUserResponse;
import dsm.project.findapple.payload.response.ChatRoomResponse;
import dsm.project.findapple.payload.response.JoinRoomResponse;
import dsm.project.findapple.utils.FcmUtil;
import dsm.project.findapple.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final BanUserRepository banUserRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;

    private final JwtProvider jwtProvider;
    private final FcmUtil fcmUtil;

    private final int PAGE_SIZE = 50;

    @Override
    public void banUser(String token, Long kakaoId) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        User banUser = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(UserNotFoundException::new);

        BanUser banUser1 = banUserRepository.findByUserAndBanUser(user, banUser)
                .orElse(null);

        if(banUser1 != null)
            throw new AlreadyBanUserException();

        banUserRepository.save(
                BanUser.builder()
                        .banedAt(LocalDateTime.now())
                        .banUser(banUser)
                        .user(user)
                        .build()
        );

        List<String> deviceTokens = deviceTokenRepository.getUserDeviceToken(banUser);

        fcmUtil.sendPushMessage(deviceTokens, user.getKakaoNickName() + "님에게 차단당하셨습니다.", "더 이상 " + user.getKakaoNickName() + "님과 대화할 수 없습니다.");

        log.info("ban success");
    }

    @Override
    public List<BanUserResponse> getMyBanList(String token, int pageNum) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<BanUser> banUsers = banUserRepository.findAllByUser(user, PageRequest.of(pageNum, PAGE_SIZE));
        List<BanUserResponse> banUserResponses = new ArrayList<>();

        for(BanUser banUser : banUsers) {
            banUserResponses.add(
                    BanUserResponse.builder()
                            .kakaoId(banUser.getBanUser().getKakaoId())
                            .banUserName(banUser.getBanUser().getKakaoNickName())
                            .build()
            );
        }

        return banUserResponses;
    }

    @Override
    @Transactional
    public void cancelBan(String token, Long kakaoId) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        User targetUser = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(UserNotFoundException::new);

        banUserRepository.findByUserAndBanUser(user, targetUser)
                .orElseThrow(BanUserNotFoundException::new);

        banUserRepository.deleteByBanUserAndUser(targetUser, user);
    }

    @Override
    public JoinRoomResponse joinRoom(String token, Long kakaoId) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        User targetUser = userRepository.findByKakaoId(kakaoId)
                .orElseThrow(UserNotFoundException::new);

        String chatId = UUID.randomUUID().toString();

        Chat chat = chatRepository.save(
                Chat.builder()
                        .chatId(chatId)
                        .build()
        );

        chatUserRepository.save(
                ChatUser.builder()
                        .chat(chat)
                        .user(user)
                        .build()
        );

        chatUserRepository.save(
                ChatUser.builder()
                        .user(targetUser)
                        .chat(chat)
                        .build()
        );

        return JoinRoomResponse.builder()
                .chatId(chatId)
                .build();
    }

    @Override
    public List<ChatRoomResponse> getMyChatRoom(String token, int pageNum) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<ChatUser> chatUserPage = chatUserRepository.findAllByUser(user, PageRequest.of(pageNum, PAGE_SIZE));
        List<ChatRoomResponse> chatRoomResponses = new ArrayList<>();

        for(ChatUser chatUser : chatUserPage) {
            Chat chat = chatUser.getChat();

            ChatUser targetUser = chatUserRepository.findByChatAndUserNot(chat, user)
                    .orElseThrow(ChatUserNotFoundException::new);

            Message message = messageRepository.findTopByChatOrderBySendAtDesc(chat);

            chatRoomResponses.add(
                    ChatRoomResponse.builder()
                            .chatId(chat.getChatId())
                            .isBan(banUserRepository.existsByUserAndBanUser(user, targetUser.getUser()))
                            .targetProfileUrl(targetUser.getUser().getProfileUrl())
                            .title(targetUser.getUser().getKakaoNickName())
                            .topMessage(message == null ? "" : message.getMessage())
                            .targetId(targetUser.getUser().getKakaoId())
                            .build()
            );
        }

        return chatRoomResponses;
    }
}
