package cn.edu.swpu.cins.controller.backend;

import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.ProduceService;
import cn.edu.swpu.cins.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    private UserService userService;
    private ProduceService produceService;

    @Autowired
    public ProductManageController(UserService userService, ProduceService produceService) {
        this.userService = userService;
        this.produceService = produceService;
    }

    @RequestMapping(value = "/save", method = RequestMethod.PUT)
    @ResponseBody
    public HttpResult productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "user need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.saveOrUpdateProduct(product);
        } else {
            return HttpResult.createByErrorMessage("save product need admin authority");
        }
    }

    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult setSaleStatus(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "user need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.setSaleStatus(product.getId(), product.getStatus());
        } else {
            return HttpResult.createByErrorMessage("set product status need admin authority");
        }
    }

    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "user need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.getProductDetail(productId);
        } else {
            return HttpResult.createByErrorMessage("set product status need admin authority");
        }
    }

    @RequestMapping(value = "/getProductList", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult getProductList(HttpSession session,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "user need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.getProductList(pageNum, pageSize);
        } else {
            return HttpResult.createByErrorMessage("set product status need admin authority");
        }
    }

}