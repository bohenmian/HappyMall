package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.UserMapper;
import cn.edu.swpu.cins.dto.request.SignInUser;
import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.service.PasswordService;
import cn.edu.swpu.cins.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
            return HttpResult.createByErrorMessage("username is not exited");
        }
        User user = userMapper.checkPassword(signInUser.getUsername());
        boolean isMatch = passwordService.match(signInUser.getPassword(), user.getPassword());
        if (!isMatch) {
            return HttpResult.createByErrorMessage("password error");
        } else {
            return HttpResult.createBySuccess("login success", user);
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
            return HttpResult.createByErrorMessage("signUp fail");
        }
        return HttpResult.createBySuccessMessage("signUp success");
    }

    public HttpResult<String> checkValid(String str, String type) {
        if (!StringUtils.isNotBlank(type)) {
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return HttpResult.createByErrorMessage("username is exited");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return HttpResult.createByErrorMessage("email is exited");
                }
            }
        } else {
            return HttpResult.createByErrorMessage("illeage parameter");
        }
        return HttpResult.createBySuccessMessage("valid success");
    }

    public HttpResult getQuestion(String username) {
        HttpResult result = this.checkValid(username, Const.USERNAME);
        if (result.isSuccess()) {
            return HttpResult.createByErrorMessage("user is not exited");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return HttpResult.createBySuccess(question);
        }
        return HttpResult.createByErrorMessage("question is null");
    }


}
