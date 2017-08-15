package cn.edu.swpu.cins.controller.portal;

import cn.edu.swpu.cins.dto.request.SignInUser;
import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.UserService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping("/user/")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<User> login(SignInUser signInUser, HttpSession session) {
        HttpResult<User> result = userService.login(signInUser);
        if (result.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, result.getData());
        }
        return result;
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return HttpResult.createBySuccess();
    }

    @RequestMapping(value = "signUp", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<String> signUp(User user) {
        return userService.signUp(user);
    }


    @RequestMapping(value = "getUserInfo", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return HttpResult.createBySuccess(user);
        }
        return HttpResult.createByErrorMessage("User not login");
    }

    @RequestMapping(value = "getQuestion", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult<String> getQuestion(String username) {
        return userService.getQuestion(username);
    }

    @RequestMapping(value = "checkAnswer", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<String> checkAnswer(String username, String question, String answer) {
        return userService.checkAnswer(username, question, answer);
    }

    @RequestMapping(value = "forgetResetPassword", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        return userService.fofgetResetPassword(username, newPassword, forgetToken);
    }

    @RequestMapping(value = "resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<String> resetPassword(HttpSession session, String password, String newPassword) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorMessage("User is not login");
        }
        return userService.resetPassword(password, newPassword, user);
    }

    @RequestMapping(value = "updateUser", method = RequestMethod.PUT)
    @ResponseBody
    public HttpResult<User> updateUser(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return HttpResult.createByErrorMessage("User is not login");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        HttpResult<User> result = userService.updateUser(currentUser);
        if (result.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, result.getData());
        }
        return result;
    }

    @RequestMapping(value = "getUserDetail", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult<User> getUserDetail(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "need login");
        }
        return userService.getUserDetail(currentUser.getId());
    }

}
