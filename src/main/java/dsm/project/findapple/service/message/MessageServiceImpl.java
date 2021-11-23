package dsm.project.findapple.service.message;

import dsm.project.findapple.entity.chat.Chat;
import dsm.project.findapple.entity.chat.ChatRepository;
import dsm.project.findapple.entity.chat_user.ChatUserRepository;
import dsm.project.findapple.entity.images.message.MessageImage;
import dsm.project.findapple.entity.images.message.MessageImageRepository;
import dsm.project.findapple.entity.message.Message;
import dsm.project.findapple.entity.message.MessageRepository;
import dsm.project.findapple.entity.promise.Promise;
import dsm.project.findapple.entity.promise.PromiseRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.*;
import dsm.project.findapple.payload.enums.MessageType;
import dsm.project.findapple.payload.response.MessageResponse;
import dsm.project.findapple.payload.response.SendImageResponse;
import dsm.project.findapple.service.image.ImageService;
import dsm.project.findapple.utils.JwtProvider;
import dsm.project.findapple.utils.ValidateImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final PromiseRepository promiseRepository;
    private final MessageRepository messageRepository;
    private final MessageImageRepository messageImageRepository;

    private final JwtProvider jwtProvider;
    private final ImageService imageService;
    private final ValidateImage validateImage;

    private final int PAGE_SIZE = 50;

    @Override
    public List<MessageResponse> getMessages(String token, String chatId, int pageNum) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Chat chat = chatRepository.findByChatId(chatId)
                .orElseThrow(ChatNotFoundException::new);

        chatUserRepository.findByChatAndUser(chat, user)
                .orElseThrow(ChatUserNotFoundException::new);

        Page<Message> messages = messageRepository.findAllByChatOrderBySendAtDesc(chat, PageRequest.of(pageNum, PAGE_SIZE));
        List<MessageResponse> messageResponses = new ArrayList<>();

        for (Message message : messages) {
            MessageImage messageImage = null;
            Promise promise = null;
            if(message.getMessageType().equals(MessageType.IMAGE)) {
                messageImage = messageImageRepository.findByMessage(message)
                        .orElseThrow(MessageImageNotFoundException::new);
            }

            if(message.getMessageType().equals(MessageType.PROMISE)) {
                promise = promiseRepository.findByMessage(message)
                        .orElseThrow(PromiseNotFoundException::new);
            }

            User user1 = message.getUser();

            messageResponses.add(
                    MessageResponse.builder()
                            .chatId(message.getChat().getChatId())
                            .messageId(message.getMessageId())
                            .messageImageName(messageImage == null ? "" : messageImage.getImageName())
                            .messageType(message.getMessageType())
                            .profileUrl(user1 != null ? user1.getProfileUrl() : "")
                            .promiseId(promise == null ? -1 : promise.getPromiseId())
                            .sendDate(message.getSendAt().toLocalDate().toString())
                            .sendTime(message.getSendAt().toLocalTime().toString())
                            .message(message.getMessage())
                            .sendUserName(user1 != null ? user1.getKakaoNickName() : "")
                            .build()
            );
        }

        Collections.reverse(messageResponses);

        return messageResponses;
    }

    @Override
    public SendImageResponse sendImage(String token, String chatId, MultipartFile image) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Chat chat = chatRepository.findByChatId(chatId)
                .orElseThrow(ChatNotFoundException::new);

        chatUserRepository.findByChatAndUser(chat, user)
                .orElseThrow(ChatUserNotFoundException::new);

        Message message = messageRepository.save(
                Message.builder()
                        .chat(chat)
                        .message("사진을 보냈습니다.")
                        .messageType(MessageType.IMAGE)
                        .user(user)
                        .sendAt(LocalDateTime.now())
                        .build()
        );

        messageImageRepository.save(
                MessageImage.builder()
                        .imageName(validateImage.validateImage(image))
                        .message(message)
                        .build()
        );

        return SendImageResponse.builder()
                .messageId(message.getMessageId())
                .build();
    }

    @Override
    @Transactional
    public void deleteMessage(String token, Long messageId) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Message message = messageRepository.findByMessageId(messageId)
                .orElseThrow(MessageNotFoundException::new);

        if(!message.getUser().equals(user))
            throw new NotYourException();

        if(message.getMessageType().equals(MessageType.IMAGE)) {
            MessageImage messageImage = messageImageRepository.findByMessage(message)
                    .orElseThrow(MessageImageNotFoundException::new);

            imageService.delete(messageImage.getImageName());

            messageRepository.save(
                    message.updateMessage("삭제된 메세지 입니다.")
            );

            messageImageRepository.deleteByMessage(message);
        }else if(message.getMessageType().equals(MessageType.PROMISE)) {
            Promise promise = promiseRepository.findByMessage(message)
                    .orElseThrow(RuntimeException::new);

            promiseRepository.deleteByMessage(promise.getMessage());

            messageRepository.save(
                    message.updateMessage("삭제된 메세지 입니다.")
            );
        }else if(message.getMessageType().equals(MessageType.MESSAGE)) {
            messageRepository.save(
                    message.updateMessage("삭제된 메세지 입니다.")
            );
        }else
            throw new DoNotHaveAuthorityException();
    }
}
