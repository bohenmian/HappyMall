package cn.edu.swpu.cins.controller.portal;

import cn.edu.swpu.cins.dto.http.Const;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.dto.view.CartVo;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping(value = "/getList", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult<CartVo> getList(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.getList(user.getId());
    }


    @RequestMapping(value = "/addProductToCart", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<CartVo> addProductToCart(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.add(user.getId(), count, productId);
    }

    @RequestMapping(value = "/updateCart", method = RequestMethod.PUT)
    @ResponseBody
    public HttpResult<CartVo> updateCart(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.updateCart(user.getId(), productId, count);
    }

    @RequestMapping(value = "/deleteProduct", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult<CartVo> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.selectAll(user.getId(), null, Const.Cart.CHECKED);
    }

    @RequestMapping(value = "/unSelectAll", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.selectAll(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @RequestMapping(value = "/select", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<CartVo> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.selectAll(user.getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping(value = "/unSelect", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult<CartVo> unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return cartService.selectAll(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

}
