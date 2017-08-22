package cn.edu.swpu.cins.controller.portal;

import cn.edu.swpu.cins.dto.http.Const;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Shipping;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shipping")
public class ShippingController {

    private ShippingService shippingService;

    @Autowired
    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @RequestMapping(value = "/addAddress", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult addAddress(HttpSession session, @RequestBody Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return shippingService.addAddress(user.getId(), shipping);
    }

    @RequestMapping(value = "/deleteAddress", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult deleteAddress(HttpSession session, Integer shippingId ) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return shippingService.deleteAddress(user.getId(), shippingId);
    }


    @RequestMapping(value = "/updateAddress", method = RequestMethod.PUT)
    @ResponseBody
    public HttpResult updateAddress(HttpSession session, @RequestBody Shipping shipping) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        return shippingService.updateAddress(user.getId(), shipping);
    }
}
