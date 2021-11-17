package dsm.project.findapple.service.user;

import dsm.project.findapple.payload.response.UserResponse;

public interface UserService {
    UserResponse getMyInfo(String token);
}
