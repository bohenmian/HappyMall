package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.request.SignInUser;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.User;

public interface UserService {

    HttpResult<User> login(SignInUser signInUser);

    HttpResult<String> signUp(User user);
}
