package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.config.MD5Config;
import cn.edu.swpu.cins.dao.UserMapper;
import cn.edu.swpu.cins.dto.http.Const;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.dto.http.TokenCache;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.exception.EmailNotExitedException;
import cn.edu.swpu.cins.exception.HappyMallException;
import cn.edu.swpu.cins.exception.UserNotExitedException;
import cn.edu.swpu.cins.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public HttpResult<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            throw new UserNotExitedException("user not exited");
        }
        String md5Password = MD5Config.MD5EncodeUtf8(password);
        User user = userMapper.checkPasswordByUsername(username, md5Password);
        if (user == null) {
            return HttpResult.createByErrorMessage("wrong password");
        }
        user.setPassword(StringUtils.EMPTY);
        return HttpResult.createBySuccess("login success", user);
    }

    @Transactional(rollbackFor = {DataAccessException.class, HappyMallException.class})
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
        user.setPassword(MD5Config.MD5EncodeUtf8(user.getPassword()));
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
                    throw new UserNotExitedException("user not exited");
                }
            }
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    throw new EmailNotExitedException("email not exited");
                }
            }
        } else {
            return HttpResult.createByErrorMessage("Illegal parameter");
        }
        return HttpResult.createBySuccessMessage("Valid success");
    }

    public HttpResult getQuestion(String username) {
        HttpResult result = this.checkValid(username, Const.USERNAME);
        if (result.isSuccess()) {
            throw new UserNotExitedException("user not exited");
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

    @Transactional(rollbackFor = {DataAccessException.class, HappyMallException.class})
    public HttpResult<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return HttpResult.createByErrorMessage("Illegal parameter,Token should not null");
        }
        HttpResult result = this.checkValid(username, Const.USERNAME);
        if (result.isSuccess()) {
            throw new UserNotExitedException("user not exited");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return HttpResult.createByErrorMessage("Token had expired");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String password = MD5Config.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username, password);
            if (rowCount > 0) {
                return HttpResult.createBySuccess("Reset password success");
            }
        } else {
            return HttpResult.createByErrorMessage("Wrong token");
        }
        return HttpResult.createByErrorMessage("Reset password fail");
    }

    @Transactional(rollbackFor = DataAccessException.class)
    public HttpResult<String> resetPassword(String password, String newPassword, User user) {
        int resultCount = userMapper.checkPassword(MD5Config.MD5EncodeUtf8(password), user.getId());
        if (resultCount == 0) {
            return HttpResult.createByErrorMessage("old password wrong");
        }
        user.setPassword(MD5Config.MD5EncodeUtf8(newPassword));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return HttpResult.createBySuccessMessage("update password success");
        }
        return HttpResult.createByErrorMessage("update password fail");
    }

    @Transactional(rollbackFor = {DataAccessException.class, HappyMallException.class})
    public HttpResult<User> updateUser(User user) {
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            throw new EmailNotExitedException("email not exited");
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
            throw new UserNotExitedException("user not exited");
        }
        return HttpResult.createBySuccess(user);
    }


    public HttpResult checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_CUSTOMER) {
            return HttpResult.createBySuccess();
        }
        return HttpResult.createByError();
    }

}
