package dsm.project.findapple.service.comment;

import dsm.project.findapple.entity.comment.Comment;
import dsm.project.findapple.entity.comment.repository.CommentRepository;
import dsm.project.findapple.entity.deviceToken.DeviceTokenRepository;
import dsm.project.findapple.entity.find.Find;
import dsm.project.findapple.entity.find.FindRepository;
import dsm.project.findapple.entity.lost.Lost;
import dsm.project.findapple.entity.lost.LostRepository;
import dsm.project.findapple.entity.recomment.Recomment;
import dsm.project.findapple.entity.recomment.repository.RecommentRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.UserNotFoundException;
import dsm.project.findapple.payload.enums.CommentType;
import dsm.project.findapple.payload.request.UpdateCommentRequest;
import dsm.project.findapple.payload.request.WriteCommentRequest;
import dsm.project.findapple.payload.response.CommentResponse;
import dsm.project.findapple.payload.response.ReCommentResponse;
import dsm.project.findapple.utils.FcmUtil;
import dsm.project.findapple.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final RecommentRepository recommentRepository;
    private final FindRepository findRepository;
    private final LostRepository lostRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    private final JwtProvider jwtProvider;
    private final FcmUtil fcmUtil;

    private <T>void setIfNotNull(Consumer<T> setter, T val) {
        if(val != null) {
            setter.accept(val);
        }
    }

    @Override
    public List<CommentResponse> readComment(String token, Long id, CommentType commentType) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        List<Comment> comments;

        if(commentType.equals(CommentType.FIND)) {
            Find find = findRepository.findByFindId(id)
                    .orElseThrow(RuntimeException::new);

            comments = commentRepository.findAllByFind(find);
        }else {
            Lost lost = lostRepository.findAllByLostId(id)
                    .orElseThrow(RuntimeException::new);

            comments = commentRepository.findAllByLost(lost);
        }

        List<CommentResponse> commentResponses = new ArrayList<>();

        for(Comment comment : comments) {
            List<ReCommentResponse> responses = new ArrayList<>();
            List<Recomment> recomments = recommentRepository.findAllByCommentId(comment);
            for(Recomment recomment : recomments) {
                responses.add(
                        ReCommentResponse.builder()
                                .comment(recomment.getComment())
                                .commentId(recomment.getRecommentId())
                                .recommentId(recomment.getRecommentId())
                                .nickName(recomment.getUser().getKakaoNickName())
                                .userId(recomment.getUser().getKakaoId())
                                .writeAt(recomment.getWriteAt())
                                .profileUrl(recomment.getUser().getProfileUrl())
                                .build()
                );

            }

            commentResponses.add(
                    CommentResponse.builder()
                            .commentId(comment.getCommentId())
                            .reComment(responses)
                            .comment(comment.getComment())
                            .nickName(comment.getUser().getKakaoNickName())
                            .userId(comment.getUser().getKakaoId())
                            .writeAt(comment.getWriteAt())
                            .profileUrl(comment.getUser().getProfileUrl())
                            .build()
            );
        }

        return commentResponses;
    }

    @Override
    public void writeComment(String token, Long id, CommentType commentType, WriteCommentRequest writeCommentRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Find find = null;
        Lost lost = null;

        if(commentType.equals(CommentType.FIND)) {
            find = findRepository.findByFindId(id)
                    .orElseThrow(RuntimeException::new);
        }else {
            lost = lostRepository.findAllByLostId(id)
                    .orElseThrow(RuntimeException::new);
        }

        commentRepository.save(
                Comment.builder()
                        .comment(writeCommentRequest.getComment())
                        .user(user)
                        .writeAt(LocalDateTime.now())
                        .find(find)
                        .lost(lost)
                        .build()
        );

        User user1 = null;

        if(find != null) {
            user1 = find.getUser();
        }

        if(lost != null) {
            user1 = lost.getUser();
        }

        List<String> deviceTokens = deviceTokenRepository.getUserDeviceToken(user1);

        fcmUtil.sendPushMessage(deviceTokens, "댓글알림!", "읽지 않은 댓글이 왔습니다.");
    }

    @Override
    public void writeReComment(String token, Long commentId, WriteCommentRequest writeCommentRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(RuntimeException::new);

        recommentRepository.save(
                Recomment.builder()
                        .commentId(comment)
                        .comment(writeCommentRequest.getComment())
                        .writeAt(LocalDateTime.now())
                        .user(user)
                        .build()
        );

        Find find = comment.getFind();
        Lost lost = comment.getLost();

        User commenter = userRepository.findByKakaoId(comment.getUser().getKakaoId())
                .orElseThrow(UserNotFoundException::new);

        User user1 = null;

        if(find != null) {
            user1 = find.getUser();
        }

        if(lost != null) {
            user1 = lost.getUser();
        }

        List<String> deviceTokens = deviceTokenRepository.getUserDeviceToken(user1);
        List<String> deviceTokens2 = deviceTokenRepository.getUserDeviceToken(commenter);

        fcmUtil.sendPushMessage(deviceTokens, "댓글알림!", "읽지 않은 댓글이 왔습니다.");

        fcmUtil.sendPushMessage(deviceTokens2, "댓글알림!", "읽지 않은 댓글이 왔습니다.");
    }

    @Override
    public void updateComment(String token, Long commentId, UpdateCommentRequest updateCommentRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(RuntimeException::new);

        if(!comment.getUser().equals(user))
            throw new RuntimeException();

        setIfNotNull(comment::setComment, updateCommentRequest.getComment());

        commentRepository.save(comment);

    }

    @Override
    public void updateReComment(String token, Long recommentId, UpdateCommentRequest updateCommentRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Recomment recomment = recommentRepository.findByRecommentId(recommentId)
                .orElseThrow(RuntimeException::new);

        if(!recomment.getUser().equals(user))
            throw new RuntimeException();

        setIfNotNull(recomment::setComment, updateCommentRequest.getComment());

        recommentRepository.save(recomment);
    }

    @Override
    @Transactional
    public void deleteComment(String token, Long commentId) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findByCommentId(commentId)
                .orElseThrow(RuntimeException::new);

        if(!comment.getUser().equals(user))
            throw new RuntimeException();

        recommentRepository.deleteAllByCommentId(comment);
        commentRepository.deleteByCommentId(commentId);
    }

    @Override
    @Transactional
    public void deleteReComment(String token, Long reCommentId) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(RuntimeException::new);

        Recomment recomment = recommentRepository.findByRecommentId(reCommentId)
                .orElseThrow(RuntimeException::new);

        if(!recomment.getUser().equals(user))
            throw new RuntimeException("not have authority");

        recommentRepository.deleteByRecommentId(reCommentId);
    }
}
