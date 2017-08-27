package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.config.MD5Config;
import cn.edu.swpu.cins.dao.UserMapper;
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
import static org.mockito.Mockito.mock;
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
        String password = "password";
        User user = mock(User.class);
        when(user.getId()).thenReturn(2);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        when(user.getPhone()).thenReturn("13541552447");
        when(user.getQuestion()).thenReturn("123");
        when(user.getAnswer()).thenReturn("123");
        when(user.getCreateTime()).thenReturn(null);
        when(user.getUpdateTime()).thenReturn(null);
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
        User user = mock(User.class);
        when(user.getId()).thenReturn(2);
        when(user.getUsername()).thenReturn("username");
        when(user.getPassword()).thenReturn("password");
        when(user.getEmail()).thenReturn("email");
        when(user.getPhone()).thenReturn("13541552447");
        when(user.getQuestion()).thenReturn("123");
        when(user.getAnswer()).thenReturn("123");
        when(user.getCreateTime()).thenReturn(null);
        when(user.getUpdateTime()).thenReturn(null);
        when(userMapper.selectByPrimaryKey(userId)).thenReturn(user);
        HttpResult result = userService.getUserDetail(userId);
        assertThat(result.getStatus(), is(0));
        verify(userMapper).selectByPrimaryKey(userId);
    }
}