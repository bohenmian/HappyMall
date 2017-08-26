package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.config.MD5Config;
import cn.edu.swpu.cins.dao.UserMapper;
import cn.edu.swpu.cins.dto.http.Const;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {


    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl(userMapper);
    }

    @Test
    public void test_login_success() throws Exception {
        String username = "username";
        String password = "password";
        when(userMapper.checkUsername(username)).thenReturn(1);
        String md5Password = MD5Config.MD5EncodeUtf8(password);
        User user = new User(username, md5Password);
        when(userMapper.checkPasswordByUsername(username, md5Password)).thenReturn(user);
        HttpResult httpResult = userService.login(username, password);
        assertThat(httpResult.getStatus(), is(0));
        verify(userMapper).checkUsername(username);
        verify(userMapper).checkPasswordByUsername(username, md5Password);
    }

    @Test
    public void test_checkAnswer_success() throws Exception {
        String username = "username";
        String question = "123";
        String answer = "321";
        when(userMapper.checkAnswer(username, question, answer)).thenReturn(1);
        HttpResult result = userService.checkAnswer(username, question, answer);
        assertThat(result.getStatus(), is(0));
        verify(userMapper).checkAnswer(username, question, answer);
    }

    @Test
    public void test_resetPassword_success() throws Exception {
        Integer id = 2;
        String username = "username";
        String password = "password";
        String email = "bohenmian@gmail.com";
        String phone = "13541552447";
        String question = "123";
        String answer = "123";
        User user = new User(id, username, password, email, phone, question, answer, 1, null, null);
        String newPassword = "newPassword";
        when(userMapper.checkPassword(MD5Config.MD5EncodeUtf8(password), user.getId())).thenReturn(1);
        user.setPassword(MD5Config.MD5EncodeUtf8(newPassword));
        when(userMapper.updateByPrimaryKeySelective(user)).thenReturn(1);
        HttpResult result = userService.resetPassword(password, newPassword, user);
        assertThat(result.getStatus(), is(0));
        verify(userMapper).checkPassword(MD5Config.MD5EncodeUtf8(password), user.getId());
        verify(userMapper).updateByPrimaryKeySelective(user);
    }

    @Test
    public void test_getUserDetail_success() throws Exception {
        Integer userId = 2;
        Integer id = 2;
        String username = "username";
        String password = "password";
        String email = "bohenmian@gmail.com";
        String phone = "13541552447";
        String question = "123";
        String answer = "123";
        User user = new User(id, username, password, email, phone, question, answer, Const.Role.ROLE_CUSTOMER, null, null);
        when(userMapper.selectByPrimaryKey(userId)).thenReturn(user);
        HttpResult result = userService.getUserDetail(userId);
        assertThat(result.getStatus(), is(0));
        verify(userMapper).selectByPrimaryKey(userId);
    }

}