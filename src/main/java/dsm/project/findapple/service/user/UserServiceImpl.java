package dsm.project.findapple.service.user;

import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.comment.Comment;
import dsm.project.findapple.entity.find.Find;
import dsm.project.findapple.entity.find.FindRepository;
import dsm.project.findapple.entity.lost.Lost;
import dsm.project.findapple.entity.lost.LostRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.UserNotFoundException;
import dsm.project.findapple.payload.response.LostResponse;
import dsm.project.findapple.payload.response.TopCommentResponse;
import dsm.project.findapple.payload.response.UserResponse;
import dsm.project.findapple.utils.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final LostRepository lostRepository;
    private final FindRepository findRepository;

    private final JwtProvider jwtProvider;

    private List<LostResponse> setLostResponses(List<Lost> losts) {
        List<LostResponse> lostResponses = new ArrayList<>();

        losts.forEach(lost -> {
            List<String> imageName = new ArrayList<>();

            lost.getLostImages().forEach(lostImage -> {
                imageName.add(lostImage.getLostImageName());
            });

            Comment topComment = lost.getComments().get(lost.getComments().size() - 1);

            lostResponses.add(
                    LostResponse.builder()
                            .lostId(lost.getLostId())
                            .category(lost.getCategory())
                            .detail(lost.getDetailInfo())
                            .kakaoId(lost.getUser().getKakaoId())
                            .lostImages(imageName)
                            .lostAt(lost.getLostAt())
                            .latitude(lost.getArea().getLatitude())
                            .longitude(lost.getArea().getLongitude())
                            .lostUser(lost.getUser().getKakaoNickName())
                            .title(lost.getTitle())
                            .writeAt(lost.getWriteAt().toLocalDate())
                            .topComment(
                                    TopCommentResponse.builder()
                                            .postId(lost.getLostId())
                                            .commentId(topComment.getCommentId())
                                            .comment(topComment.getComment())
                                            .nickName(topComment.getUser().getKakaoNickName())
                                            .userId(topComment.getUser().getKakaoId())
                                            .writeAt(topComment.getWriteAt())
                                            .build()
                            )
                            .build()
            );
        });

        return lostResponses;
    }

    @Override
    public UserResponse getMyInfo(String token) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        int lostNum = lostRepository.countAllByUser(user);
        int findNum = findRepository.countAllByUser(user);

        List<Lost> myLosts = lostRepository.findAllByUser(user);

        return UserResponse.builder()
                .kakaoId(user.getKakaoId())
                .findapplePoint(user.getPoint())
                .findNum(findNum)
                .lostNum(lostNum)
                .myLosts(setLostResponses(myLosts))
                .profileUrl(user.getProfileUrl())
                .nickName(user.getKakaoNickName())
                .build();
    }
}
