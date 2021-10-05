package dsm.project.findapple.service.user;

import dsm.project.findapple.payload.request.SignUpRequest;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);
}
