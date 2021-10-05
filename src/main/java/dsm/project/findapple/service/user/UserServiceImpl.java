package dsm.project.findapple.service.user;

import dsm.project.findapple.entity.area.Area;
import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.user.User;
import dsm.project.findapple.entity.user.UserRepository;
import dsm.project.findapple.payload.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;

    @Override
    @Transactional
    public void signUp(SignUpRequest signUpRequest) {
        if(userRepository.existsByKakaoId(signUpRequest.getKakaoId()))
            throw new RuntimeException("user already join");

        User user = User.builder()
                .kakaoId(signUpRequest.getKakaoId())
                .kakaoNickName(signUpRequest.getKakaoNickName())
                .point(0)
                .build();

        Area area = Area.builder()
                .latitude(signUpRequest.getLatitude())
                .longitude(signUpRequest.getLongitude())
                .build();

        userRepository.save(user);
        areaRepository.save(area);
    }
}
