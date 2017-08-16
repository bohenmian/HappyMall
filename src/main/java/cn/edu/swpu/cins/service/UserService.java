package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.request.SignInUser;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.User;

public interface UserService {

    HttpResult<User> login(SignInUser signInUser);

    HttpResult<String> signUp(User user);

    HttpResult<String> checkValid(String str, String type);

    HttpResult<String> getQuestion(String username);

    HttpResult<String> checkAnswer(String username, String question, String answer);

    HttpResult<String> fofgetResetPassword(String username, String newPassword, String forgetToken);

    HttpResult<String> resetPassword(String password, String newPassword, User user);

    HttpResult<User> updateUser(User user);

    HttpResult<User> getUserDetail(Integer userId);

    HttpResult checkAdminRole(User user);
}
