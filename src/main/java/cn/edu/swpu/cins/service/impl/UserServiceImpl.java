package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.UserMapper;
import cn.edu.swpu.cins.dto.request.SignInUser;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.service.PasswordService;
import cn.edu.swpu.cins.service.UserService;
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
            return HttpResult.createByErrorMessage("username not exited");
        }
        User user = userMapper.checkPassword(signInUser.getUsername());
        boolean isMatch = passwordService.match(signInUser.getPassword(), user.getPassword());
        if (!isMatch) {
            return HttpResult.createByErrorMessage("password error");
        } else {
            return HttpResult.createBySuccess("login success", user);
        }
    }
}
