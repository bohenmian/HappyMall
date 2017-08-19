package cn.edu.swpu.cins.controller.backend;

import cn.edu.swpu.cins.config.PropertiesConfig;
import cn.edu.swpu.cins.dto.response.Const;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.FileService;
import cn.edu.swpu.cins.service.ProduceService;
import cn.edu.swpu.cins.service.UserService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    private UserService userService;
    private ProduceService produceService;
    private FileService fileService;

    @Autowired
    public ProductManageController(UserService userService, ProduceService produceService,
                                   FileService fileService) {
        this.userService = userService;
        this.produceService = produceService;
        this.fileService = fileService;
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

    @RequestMapping(value = "/searchProduct", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult searchProduct(HttpSession session, String productName, Integer productId,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), HttpResultEnum.NEED_LOGIN.getDescrption());
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return produceService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return HttpResult.createByErrorMessage("set product status need admin authority");
        }
    }

    @RequestMapping("/upload")
    @ResponseBody
    public HttpResult upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file,HttpServletRequest request){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(),"user need login in");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesConfig.getProperties("ftp.server.http.prefix")+targetFileName;

            Map fileMap = Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);
            return HttpResult.createBySuccess(fileMap);
        }else{
            return HttpResult.createByErrorMessage("set product status need admin authority");
        }
    }

}