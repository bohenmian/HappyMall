package cn.edu.swpu.cins.controller.backend;

import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.CategoryService;
import cn.edu.swpu.cins.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    private UserService userService;
    private CategoryService categoryService;

    @Autowired
    public CategoryManageController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "addCategory", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "User need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.addCategory(categoryName, parentId);
        } else {
            return HttpResult.createByErrorMessage("Need admin authority");
        }
    }

    @RequestMapping(value = "updateCategory", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "User need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.updateCategoryName(categoryName, categoryId);
        } else {
            return HttpResult.createByErrorMessage("Need admin authority");
        }
    }

    @RequestMapping(value = "getChildrenCategory", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "User need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.getChildrenParallelCategory(categoryId);
        } else {
            return HttpResult.createByErrorMessage("Need admin authority");
        }
    }

    @RequestMapping(value = "getCategory", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "User need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return categoryService.getCategory(categoryId);
        } else {
            return HttpResult.createByErrorMessage("Need admin authority");
        }
    }

}
