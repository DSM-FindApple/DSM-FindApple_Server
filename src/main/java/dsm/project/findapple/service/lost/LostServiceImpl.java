package dsm.project.findapple.service.lost;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.comment.Comment;
import dsm.project.findapple.entity.comment.repository.CommentRepository;
import dsm.project.findapple.entity.images.find.FindImageRepository;
import dsm.project.findapple.entity.images.lost.LostImage;
import dsm.project.findapple.entity.images.lost.LostImageRepository;
import dsm.project.findapple.entity.lost.Lost;
import dsm.project.findapple.entity.lost.LostRepository;
import dsm.project.findapple.entity.lost.RelationLostRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.LostNotFoundException;
import dsm.project.findapple.error.exceptions.UserNotFoundException;
import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.request.AreaRequest;
import dsm.project.findapple.payload.request.UpdateLostRequest;
import dsm.project.findapple.payload.request.WriteLostRequest;
import dsm.project.findapple.payload.response.FindResponse;
import dsm.project.findapple.payload.response.LostResponse;
import dsm.project.findapple.payload.response.TopCommentResponse;
import dsm.project.findapple.service.image.ImageService;
import dsm.project.findapple.utils.JwtProvider;
import dsm.project.findapple.utils.KoreanDecoder;
import dsm.project.findapple.utils.ValidateImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class LostServiceImpl implements LostService {

    private final LostRepository lostRepository;
    private final LostImageRepository lostImageRepository;
    private final FindImageRepository findImageRepository;
    private final UserRepository userRepository;
    private final AreaRepository areaRepository;
    private final RelationLostRepository relationLostRepository;
    private final CommentRepository commentRepository;

    private final JwtProvider jwtProvider;
    private final ImageService s3Service;
    private final ValidateImage validateImage;
    private final KoreanDecoder koreanDecoder;

    private final int PAGE_SIZE = 30;

    private <T>void setIfNotNull(Consumer<T> setter, T value) {
        if(value != null)
            setter.accept(value);
    }

    private List<LostResponse> setLostResponses(List<Lost> losts) {
        List<LostResponse> lostResponses = new ArrayList<>();

        losts.forEach(lost -> {
            List<String> imageName = new ArrayList<>();

            lost.getLostImages().forEach(lostImage -> {
                imageName.add(lostImage.getLostImageName());
            });

            Comment topComment = null;

            if(!lost.getComments().isEmpty()) {
               topComment = lost.getComments().get(lost.getComments().size() - 1);
            }

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
                            .profileUrl(lost.getUser().getProfileUrl())
                            .topComment(
                                    topComment == null ? null :
                                    TopCommentResponse.builder()
                                            .postId(lost.getLostId())
                                            .commentId(topComment.getCommentId())
                                            .profileUrl(topComment.getUser().getProfileUrl())
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
    @Transactional
    public void writeLost(String token, WriteLostRequest writeLostRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Area area = areaRepository.save(
                Area.builder()
                        .longitude(writeLostRequest.getLongitude())
                        .latitude(writeLostRequest.getLatitude())
                        .build()
        );

        Lost lost = lostRepository.save(
                Lost.builder()
                        .area(area)
                        .title(writeLostRequest.getTitle())
                        .category(writeLostRequest.getCategory())
                        .lostAt(writeLostRequest.getLostAt())
                        .writeAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                        .user(user)
                        .detailInfo(writeLostRequest.getDetail())
                        .build()
        );

        List<LostImage> lostImages = new ArrayList<>();

        writeLostRequest.getImages().forEach(image -> {
            lostImages.add(
                    LostImage.builder()
                            .lost(lost)
                            .lostImageName(validateImage.validateImage(image))
                            .build()
            );
        });

        lostImageRepository.saveAll(lostImages);
    }

    @Override
    public void updateLost(String token, Long lostId, UpdateLostRequest updateLostRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Lost lost = lostRepository.findAllByLostId(lostId)
                .orElseThrow(LostNotFoundException::new);

        if(lost.getUser().equals(user))
            throw new LostNotFoundException();

        setIfNotNull(lost::setTitle, updateLostRequest.getTitle());
        setIfNotNull(lost::setDetailInfo, updateLostRequest.getDetail());
        setIfNotNull(lost::setCategory, updateLostRequest.getCategory());
        setIfNotNull(lost.getArea()::setLatitude, updateLostRequest.getLatitude());
        setIfNotNull(lost.getArea()::setLongitude, updateLostRequest.getLongitude());
        setIfNotNull(lost::setLostAt, updateLostRequest.getLostAt());

        lostRepository.save(lost);
    }

    @Override
    public void updateLostImage(String token, Long lostId, List<MultipartFile> lostImages) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Lost lost = lostRepository.findAllByLostId(lostId)
                .orElseThrow(LostNotFoundException::new);

        lost.getLostImages().forEach(lostImage -> {
            s3Service.delete(lostImage.getLostImageName());
        });
        lostImageRepository.deleteAllByLost_LostId(lostId);

        List<LostImage> lostImages1 = new ArrayList<>();

        lostImages.forEach(lostImage -> {
            lostImages1.add(
                    LostImage.builder()
                            .lost(lost)
                            .lostImageName(validateImage.validateImage(lostImage))
                            .build()
            );
        });

        lostImageRepository.saveAll(lostImages1);
    }

    @Override
    public List<LostResponse> getLostByArea(String token, AreaRequest areaRequest) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Double minusLongitude = areaRequest.getEndLongitude() - areaRequest.getStartLongitude();
        Double minusLatitude = areaRequest.getStartLatitude() - areaRequest.getEndLatitude();

        Double rightUpLongitude = areaRequest.getStartLongitude() + minusLongitude;
        Double leftDownLatitude = areaRequest.getStartLatitude() - minusLatitude;

        List<Lost> losts = lostRepository
                .findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(
                        areaRequest.getStartLongitude(),
                        rightUpLongitude,
                        areaRequest.getStartLatitude(),
                        leftDownLatitude
                );

        return setLostResponses(losts);
    }

    @Override
    public List<LostResponse> readLost(String token, int pageNum, AreaRequest areaRequest) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Double minusLongitude = areaRequest.getEndLongitude() - areaRequest.getStartLongitude();
        Double minusLatitude = areaRequest.getStartLatitude() - areaRequest.getEndLatitude();

        Double rightUpLongitude = areaRequest.getStartLongitude() + minusLongitude;
        Double leftDownLatitude = areaRequest.getStartLatitude() - minusLatitude;

        Page<Lost> losts = lostRepository
                .findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(
                        areaRequest.getStartLongitude(),
                        rightUpLongitude,
                        areaRequest.getStartLatitude(),
                        leftDownLatitude,
                        PageRequest.of(pageNum, PAGE_SIZE)
                );

        return setLostResponses(losts.toList());
    }

    @Override
    public List<LostResponse> searchLostByTitle(String token, String title, int pageNum) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<Lost> losts = lostRepository.findAllByTitleContaining(title, PageRequest.of(pageNum, PAGE_SIZE));

        return setLostResponses(losts.toList());
    }

    @Override
    public List<LostResponse> searchLostByCategory(String token, Category category, int pageNum) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<Lost> losts = lostRepository.findAllByCategory(category, PageRequest.of(pageNum, PAGE_SIZE));

        return setLostResponses(losts.toList());
    }

    @Override
    public List<FindResponse> readRelationLost(String token, String title, int pageNum) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        List<String> keywords = koreanDecoder.decodeKorean(title);
        StringBuilder addSql = new StringBuilder(" l.title LIKE '%" + keywords.get(0) + "%'");
        keywords.remove(0);

        for(String keyword : keywords) {
            addSql.append(" OR l.title LIKE '%").append(keyword).append("%'");
        }

        List<FindResponse> findResponses = relationLostRepository.findAllByRelation(
                String.valueOf(addSql),
                dsm.project.findapple.utils.Page.of(pageNum, PAGE_SIZE)
        );

        for(FindResponse lostResponse : findResponses) {
            Comment comment = commentRepository.findTop1ByFind_FindIdOrderByWriteAtDesc(lostResponse.getFindId())
                    .orElse(null);

            List<String> images = findImageRepository.getImageNames(lostResponse.getFindId());

            lostResponse.setFindImages(images);
            if(comment != null) {
                lostResponse.setTopComment(
                        TopCommentResponse.builder()
                                .commentId(comment.getCommentId())
                                .profileUrl(comment.getUser().getProfileUrl())
                                .writeAt(comment.getWriteAt())
                                .userId(comment.getUser().getKakaoId())
                                .nickName(comment.getUser().getKakaoNickName())
                                .postId(comment.getLost().getLostId())
                                .comment(comment.getComment())
                                .build()
                );
            }else {
                lostResponse.setTopComment(null);
            }
        }

        return findResponses;
    }

    @Override
    public List<LostResponse> getMyLost(String token, int pageNum) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(RuntimeException::new);

        Page<Lost> losts = lostRepository.findAllByUser(user, PageRequest.of(pageNum, PAGE_SIZE));

        return setLostResponses(losts.toList());
    }

    @Override
    @Transactional
    public void deleteLost(String token, Long lostId) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        lostRepository.findAllByLostId(lostId)
                .orElseThrow(LostNotFoundException::new);

        lostRepository.deleteByLostId(lostId);
    }
}
