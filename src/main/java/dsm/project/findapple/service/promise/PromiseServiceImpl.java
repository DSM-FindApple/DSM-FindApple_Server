package dsm.project.findapple.service.promise;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.chat.Chat;
import dsm.project.findapple.entity.chat.ChatRepository;
import dsm.project.findapple.entity.chat_user.ChatUserRepository;
import dsm.project.findapple.entity.message.Message;
import dsm.project.findapple.entity.message.MessageRepository;
import dsm.project.findapple.entity.promise.Promise;
import dsm.project.findapple.entity.promise.PromiseRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.*;
import dsm.project.findapple.payload.enums.MessageType;
import dsm.project.findapple.payload.request.PromiseRequest;
import dsm.project.findapple.payload.request.UpdatePromiseRequest;
import dsm.project.findapple.payload.response.PromiseResponse;
import dsm.project.findapple.payload.response.SendPromiseResponse;
import dsm.project.findapple.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class PromiseServiceImpl implements PromiseService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final PromiseRepository promiseRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageRepository messageRepository;

    private final JwtProvider jwtProvider;

    private <T>void setIfNotSame(Consumer<T> setter, T value, T value2) {
        if(value != value2) {
            setter.accept(value);
        }
    }

    @Override
    public SendPromiseResponse sendPromise(String token, String chatId, PromiseRequest promiseRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        User target = userRepository.findByKakaoId(promiseRequest.getTargetId())
                .orElseThrow(UserNotFoundException::new);

        Chat chat = chatRepository.findByChatId(chatId)
                .orElseThrow(ChatNotFoundException::new);

        chatUserRepository.findByChatAndUser(chat, user)
                .orElseThrow(ChatUserNotFoundException::new);

        Message message = messageRepository.save(
                Message.builder()
                        .sendAt(LocalDateTime.now())
                        .user(user)
                        .messageType(MessageType.PROMISE)
                        .message("약속을 잡았습니다.")
                        .chat(chat)
                        .build()
        );

        Area area = areaRepository.save(
                Area.builder()
                        .longitude(promiseRequest.getLongitude())
                        .latitude(promiseRequest.getLatitude())
                        .build()
        );

        Promise promise = promiseRepository.save(
                Promise.builder()
                        .area(area)
                        .isAccept(false)
                        .meetAt(promiseRequest.getMeetAt())
                        .user(user)
                        .target(target)
                        .message(message)
                        .script(promiseRequest.getScript())
                        .build()
        );

        return SendPromiseResponse.builder()
                .promiseId(promise.getPromiseId())
                .build();
    }

    @Override
    public List<PromiseResponse> getMyPromises(String token) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        List<Promise> promises = promiseRepository.findAllByUser(user);
        List<PromiseResponse> promiseResponses = new ArrayList<>();

        for (Promise promise : promises) {
            promiseResponses.add(
                    PromiseResponse.builder()
                            .chatId(promise.getMessage().getChat().getChatId())
                            .promiseId(promise.getPromiseId())
                            .isAccept(promise.getIsAccept())
                            .latitude(promise.getArea().getLatitude())
                            .longitude(promise.getArea().getLongitude())
                            .targetName(promise.getTarget().getKakaoNickName())
                            .targetProfileUrl(promise.getTarget().getProfileUrl())
                            .meetAt(promise.getMeetAt().toString())
                            .script(promise.getScript())
                            .kakaoId(promise.getUser().getKakaoId())
                            .build()
            );
        }

        return promiseResponses;
    }

    @Override
    public PromiseResponse getPromise(String token, Long promiseId) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Promise promise = promiseRepository.findByPromiseId(promiseId)
                .orElseThrow(PromiseNotFoundException::new);

        return PromiseResponse.builder()
                .chatId(promise.getMessage().getChat().getChatId())
                .promiseId(promise.getPromiseId())
                .isAccept(promise.getIsAccept())
                .latitude(promise.getArea().getLatitude())
                .longitude(promise.getArea().getLongitude())
                .targetName(promise.getTarget().getKakaoNickName())
                .targetProfileUrl(promise.getTarget().getProfileUrl())
                .meetAt(promise.getMeetAt().toString())
                .script(promise.getScript())
                .kakaoId(promise.getUser().getKakaoId())
                .build();
    }

    @Override
    public void acceptPromise(String token, Long promiseId, Boolean isAccept) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Promise promise = promiseRepository.findByPromiseId(promiseId)
                .orElseThrow(PromiseNotFoundException::new);

        if (!promise.getTarget().equals(user))
            throw new NotYourException();

        promiseRepository.save(promise.updateIsAccept(isAccept));
    }

    @Override
    public void updatePromise(String token, Long promiseId, String chatId, UpdatePromiseRequest updatePromiseRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Chat chat = chatRepository.findByChatId(chatId)
                .orElseThrow(ChatNotFoundException::new);

        chatUserRepository.findByChatAndUser(chat, user)
                .orElseThrow(ChatUserNotFoundException::new);

        Promise promise = promiseRepository.findByPromiseId(promiseId)
                .orElseThrow(PromiseNotFoundException::new);

        if (!promise.getUser().equals(user))
            throw new NotYourException();

        setIfNotSame(promise::setScript, updatePromiseRequest.getScript(), promise.getScript());
        setIfNotSame(promise::setMeetAt, updatePromiseRequest.getMeetAt(), promise.getMeetAt());
        setIfNotSame(promise.getArea()::setLatitude, updatePromiseRequest.getLatitude(), promise.getArea().getLatitude());
        setIfNotSame(promise.getArea()::setLongitude, updatePromiseRequest.getLongitude(), promise.getArea().getLongitude());

        areaRepository.save(promise.getArea());
        promiseRepository.save(promise);
    }
}