package dsm.project.findapple.service.user;

import dsm.project.findapple.entity.area.AreaRepository;
import dsm.project.findapple.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AreaRepository areaRepository;

}
