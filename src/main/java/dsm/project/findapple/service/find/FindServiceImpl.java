package dsm.project.findapple.service.find;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.comment.Comment;
import dsm.project.findapple.entity.comment.repository.CommentRepository;
import dsm.project.findapple.entity.find.Find;
import dsm.project.findapple.entity.find.FindRepository;
import dsm.project.findapple.entity.images.find.FindImage;
import dsm.project.findapple.entity.images.find.FindImageRepository;
import dsm.project.findapple.entity.images.find.RelationFindRepository;
import dsm.project.findapple.entity.images.lost.LostImageRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.error.exceptions.FindNotFoundException;
import dsm.project.findapple.error.exceptions.NotYourException;
import dsm.project.findapple.error.exceptions.UserNotFoundException;
import dsm.project.findapple.payload.enums.Category;
import dsm.project.findapple.payload.request.AreaRequest;
import dsm.project.findapple.payload.request.UpdateFindRequest;
import dsm.project.findapple.payload.request.WriteFindRequest;
import dsm.project.findapple.payload.response.FindResponse;
import dsm.project.findapple.payload.response.LostResponse;
import dsm.project.findapple.payload.response.TopCommentResponse;
import dsm.project.findapple.service.image.ImageService;
import dsm.project.findapple.utils.JwtProvider;
import dsm.project.findapple.utils.KoreanDecoder;
import dsm.project.findapple.utils.ValidateImage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class FindServiceImpl implements FindService {

    private final UserRepository userRepository;
    private final FindRepository findRepository;
    private final FindImageRepository findImageRepository;
    private final AreaRepository areaRepository;
    private final RelationFindRepository relationFindRepository;
    private final CommentRepository commentRepository;
    private final LostImageRepository lostImageRepository;

    private final JwtProvider jwtProvider;
    private final ValidateImage validateImage;
    private final ImageService imageService;
    private final KoreanDecoder koreanDecoder;

    private final int PAGE_SIZE = 30;

    private <T>void setIfNotNull(Consumer<T> setter, T value) {
        if(value != null)
            setter.accept(value);
    }

    private List<FindResponse> setFindResponse(List<Find> findPage) {
        List<FindResponse> responses = new ArrayList<>();

        for (Find find : findPage) {
            List<String> findImages = new ArrayList<>();
            for (FindImage findImage : find.getFindImages())
                findImages.add(findImage.getImageName());

            Comment topComment = find.getComments().get(find.getComments().size() - 1);

            responses.add(
                    FindResponse.builder()
                            .findAt(find.getFindAt())
                            .findImages(findImages)
                            .findId(find.getFindId())
                            .category(find.getCategory())
                            .detail(find.getDetailInfo())
                            .findUser(find.getUser().getKakaoNickName())
                            .kakaoId(find.getUser().getKakaoId())
                            .latitude(find.getArea().getLatitude())
                            .longitude(find.getArea().getLongitude())
                            .profileUrl(find.getUser().getProfileUrl())
                            .title(find.getTitle())
                            .topComment(
                                    TopCommentResponse.builder()
                                            .comment(topComment.getComment())
                                            .postId(find.getFindId())
                                            .nickName(topComment.getUser().getKakaoNickName())
                                            .userId(topComment.getUser().getKakaoId())
                                            .commentId(topComment.getCommentId())
                                            .writeAt(topComment.getWriteAt())
                                            .build()
                            )
                            .writeAt(find.getWriteAt().toLocalDate())
                            .build()
            );
        }

        return responses;
    }

    @Override
    public void writeFind(String token, WriteFindRequest writeFindRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Area area = areaRepository.save(
                Area.builder()
                        .latitude(writeFindRequest.getLatitude())
                        .longitude(writeFindRequest.getLongitude())
                        .build()
        );

        Find find = findRepository.save(
                Find.builder()
                        .area(area)
                        .category(writeFindRequest.getCategory())
                        .findAt(writeFindRequest.getFindAt())
                        .user(user)
                        .detailInfo(writeFindRequest.getDetail())
                        .writeAt(LocalDateTime.now())
                        .title(writeFindRequest.getTitle())
                        .build()
        );

        List<FindImage> findImages = new ArrayList<>();
        writeFindRequest.getImages().forEach(image -> {
            findImages.add(
                    FindImage.builder()
                            .find(find)
                            .imageName(validateImage.validateImage(image))
                            .build()
            );
        });

        userRepository.save(user.upPoint(1));
        findImageRepository.saveAll(findImages);
    }

    @Override
    public void updateFind(String token, Long findId, UpdateFindRequest updateFindRequest) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Find find = findRepository.findByFindId(findId)
                .orElseThrow(FindNotFoundException::new);

        if(find.getUser().equals(user))
            throw new NotYourException();

        setIfNotNull(find::setTitle, updateFindRequest.getTitle());
        setIfNotNull(find::setDetailInfo, updateFindRequest.getDetail());
        setIfNotNull(find::setCategory, updateFindRequest.getCategory());
        setIfNotNull(find.getArea()::setLatitude, updateFindRequest.getLatitude());
        setIfNotNull(find.getArea()::setLongitude, updateFindRequest.getLongitude());
        setIfNotNull(find::setFindAt, updateFindRequest.getLostAt());

        findRepository.save(find);
    }

    @Override
    public void updateFindImage(String token, Long findId, List<MultipartFile> findImages) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Find find = findRepository.findByFindId(findId)
                .orElseThrow(FindNotFoundException::new);

        find.getFindImages().forEach(findImage -> {
            imageService.delete(findImage.getImageName());
        });
        findImageRepository.deleteAllByFind(find);

        List<FindImage> findImagesList = new ArrayList<>();
        for (MultipartFile findImage : findImages) {
            findImagesList.add(
                    FindImage.builder()
                            .imageName(validateImage.validateImage(findImage))
                            .find(find)
                            .build()
            );
        }
        findImageRepository.saveAll(findImagesList);
    }

    @Override
    public List<FindResponse> getFindByArea(String token, AreaRequest areaRequest) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Double minusLongitude = areaRequest.getEndLongitude() - areaRequest.getStartLongitude();
        Double minusLatitude = areaRequest.getStartLatitude() - areaRequest.getEndLatitude();

        Double rightUpLongitude = areaRequest.getStartLongitude() + minusLongitude;
        Double leftDownLatitude = areaRequest.getStartLatitude() - minusLatitude;

        List<Find> finds = findRepository.findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(
                areaRequest.getStartLongitude(),
                rightUpLongitude,
                areaRequest.getStartLatitude(),
                leftDownLatitude
        );

        return setFindResponse(finds);
    }

    @Override
    public List<FindResponse> readFind(String token, int pageNum, AreaRequest areaRequest) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Double minusLongitude = areaRequest.getEndLongitude() - areaRequest.getStartLongitude();
        Double minusLatitude = areaRequest.getStartLatitude() - areaRequest.getEndLatitude();

        Double rightUpLongitude = areaRequest.getStartLongitude() + minusLongitude;
        Double leftDownLatitude = areaRequest.getStartLatitude() - minusLatitude;

        Page<Find> findPage = findRepository
                .findAllByArea_LongitudeGreaterThanEqualAndArea_LongitudeLessThanEqualAndArea_LatitudeGreaterThanEqualAndArea_LatitudeLessThanEqualOrderByWriteAtDesc(
                        areaRequest.getStartLongitude(),
                        rightUpLongitude,
                        areaRequest.getStartLatitude(),
                        leftDownLatitude,
                        PageRequest.of(pageNum, PAGE_SIZE)
                );

        return setFindResponse(findPage.toList());
    }

    @Override
    public List<FindResponse> searchFindByTitle(String token, String title, int pageNum) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<Find> findPage = findRepository.findAllByTitleContaining(title, PageRequest.of(pageNum, PAGE_SIZE));

        return setFindResponse(findPage.toList());
    }

    @Override
    public List<FindResponse> searchFindByCategory(String token, Category category, int pageNum) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<Find> findPage = findRepository.findAllByCategory(category, PageRequest.of(pageNum, PAGE_SIZE));

        return setFindResponse(findPage.toList());
    }

    @Override
    public List<LostResponse> readRelationFind(String token, String title, int pageNum) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        List<String> keywords = koreanDecoder.decodeKorean(title);
        StringBuilder addSql = new StringBuilder(" l.title LIKE '%" + keywords.get(0) + "%'");
        keywords.remove(0);

        for(String keyword : keywords) {
            addSql.append(" OR l.title LIKE '%").append(keyword).append("%'");
        }

        List<LostResponse> responses = relationFindRepository.findAllByRelation(
                String.valueOf(addSql),
                dsm.project.findapple.utils.Page.of(pageNum, PAGE_SIZE)
        );

        for (LostResponse response : responses) {
            Comment comment = commentRepository.findTop1ByLost_LostIdOrderByWriteAtDesc(response.getLostId())
                    .orElse(null);

            List<String> imageNames = lostImageRepository.getImageNames(response.getLostId());

            response.setLostImages(imageNames);
            if(comment != null) {
                response.setTopComment(
                        TopCommentResponse.builder()
                                .commentId(comment.getCommentId())
                                .writeAt(comment.getWriteAt())
                                .userId(comment.getUser().getKakaoId())
                                .nickName(comment.getUser().getKakaoNickName())
                                .postId(comment.getLost().getLostId())
                                .comment(comment.getComment())
                                .build()
                );
            }else {
                response.setTopComment(null);
            }
        }

        return responses;
    }

    @Override
    public List<FindResponse> getMyFind(String token, int pageNum) {
        User user = userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        Page<Find> findPage = findRepository.findAllByUser(user, PageRequest.of(pageNum, PAGE_SIZE));

        return setFindResponse(findPage.toList());
    }

    @Override
    public void deleteFind(String token, Long findId) {
        userRepository.findByKakaoId(jwtProvider.getKakaoId(token))
                .orElseThrow(UserNotFoundException::new);

        findRepository.findByFindId(findId)
                .orElseThrow(FindNotFoundException::new);

        findRepository.deleteByFindId(findId);
    }
}
