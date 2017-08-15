package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.UserMapper;
import cn.edu.swpu.cins.dto.request.SignInUser;
import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.dto.response.TokenCache;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.service.PasswordService;
import cn.edu.swpu.cins.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private PasswordService passwordService;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PasswordService passwordService) {
        this.userMapper = userMapper;
        this.passwordService = passwordService;
    }

    @Override
    public HttpResult<User> login(SignInUser signInUser) {
        int resultCount = userMapper.checkUsername(signInUser.getUsername());
        if (resultCount == 0) {
            return HttpResult.createByErrorMessage("Username is not exited");
        }
        User user = userMapper.checkPasswordByUsername(signInUser.getUsername(), signInUser.getPassword());
        boolean isMatch = passwordService.match(signInUser.getPassword(), user.getPassword());
        if (!isMatch) {
            return HttpResult.createByErrorMessage("Password error");
        } else {
            return HttpResult.createBySuccess("Login success", user);
        }
    }

    @Override
    public HttpResult<String> signUp(User user) {
        HttpResult result = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!result.isSuccess()) {
            return result;
        }
        result = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!result.isSuccess()) {
            return result;
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setPassword(passwordService.encode(user.getPassword()));
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return HttpResult.createByErrorMessage("SignUp fail");
        }
        return HttpResult.createBySuccessMessage("SignUp success");
    }

    public HttpResult<String> checkValid(String str, String type) {
        if (!StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return HttpResult.createByErrorMessage("Username is exited");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return HttpResult.createByErrorMessage("Email is exited");
                }
            }
        } else {
            return HttpResult.createByErrorMessage("Illeage parameter");
        }
        return HttpResult.createBySuccessMessage("Valid success");
    }

    public HttpResult getQuestion(String username) {
        HttpResult result = this.checkValid(username, Const.USERNAME);
        if (result.isSuccess()) {
            return HttpResult.createByErrorMessage("User is not exited");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return HttpResult.createBySuccess(question);
        }
        return HttpResult.createByErrorMessage("Question is null");
    }

    public HttpResult<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return HttpResult.createBySuccess(forgetToken);
        }
        return HttpResult.createByErrorMessage("Wrong answer");
    }

    public HttpResult<String> fofgetResetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return HttpResult.createByErrorMessage("Illeage parameter,Token should not null");
        }
        HttpResult result = this.checkValid(username, Const.USERNAME);
        if (result.isSuccess()) {
            return HttpResult.createByErrorMessage("User is not exited");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return HttpResult.createByErrorMessage("Token had expired");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String password = passwordService.encode(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username, password);
            if (rowCount > 0) {
                return HttpResult.createBySuccess("Reset password success");
            }
        } else {
            return HttpResult.createByErrorMessage("Wrong token");
        }
        return HttpResult.createByErrorMessage("Reset password fail");
    }

    public HttpResult<String> resetPassword(String password, String newPassword, User user) {
        int resultCount = userMapper.checkPassword(passwordService.encode(password), user.getId());
        if (resultCount == 0) {
            return HttpResult.createByErrorMessage("old password wrong");
        }
        user.setPassword(passwordService.encode(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return HttpResult.createBySuccessMessage("update password success");
        }
        return HttpResult.createByErrorMessage("update password fail");
    }

    public HttpResult<User> updateUser(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return HttpResult.createByErrorMessage("Email is exited");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return HttpResult.createBySuccess("Update user success", updateUser);
        }
        return HttpResult.createByErrorMessage("Update user fail");
    }

    public HttpResult<User> getUserDetail(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            return HttpResult.createByErrorMessage("User not exit");
        }
        return HttpResult.createBySuccess(user);
    }


}
