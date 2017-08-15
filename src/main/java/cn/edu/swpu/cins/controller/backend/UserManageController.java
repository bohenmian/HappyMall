package cn.edu.swpu.cins.controller.backend;


import cn.edu.swpu.cins.dto.request.SignInUser;
import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller("/manage/user")
public class UserManageController {

    private UserService userService;

    @Autowired
    public UserManageController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<User> login(SignInUser signInUser, HttpSession session) {
        HttpResult<User> result = userService.login(signInUser);
        if (result.isSuccess()) {
            User user = result.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return result;
            } else {
                return HttpResult.createByErrorMessage("User is not admin, deny login");
            }
        }
        return result;
    }
}
