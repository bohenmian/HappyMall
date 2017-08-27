package cn.edu.swpu.cins.controller.backend;

import cn.edu.swpu.cins.config.PropertiesConfig;
import cn.edu.swpu.cins.dto.http.Const;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import cn.edu.swpu.cins.entity.User;
import cn.edu.swpu.cins.enums.HttpResultEnum;
import cn.edu.swpu.cins.service.FileService;
import cn.edu.swpu.cins.service.ProductService;
import cn.edu.swpu.cins.service.UserService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    private UserService userService;
    private ProductService productService;
    private FileService fileService;

    @Autowired
    public ProductManageController(UserService userService, ProductService productService,
                                   FileService fileService) {
        this.userService = userService;
        this.productService = productService;
        this.fileService = fileService;
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.PUT)
    @ResponseBody
    public HttpResult productSave(HttpSession session, @RequestBody Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "user need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.saveOrUpdateProduct(product);
        } else {
            return HttpResult.createByErrorMessage("save product need admin authority");
        }
    }

    @RequestMapping(value = "/setStatus", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "user need login");
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            return productService.setSaleStatus(productId, status);
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
            return productService.manageProductDetail(productId);
        } else {
            return HttpResult.createByErrorMessage("need admin authority");
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
            return productService.getProductList(pageNum, pageSize);
        } else {
            return HttpResult.createByErrorMessage("need admin authority");
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
            return productService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return HttpResult.createByErrorMessage("need admin authority");
        }
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult uploadFile(HttpSession session,
                                 @RequestParam(value = "upload_file", required = false) MultipartFile file,
                                 HttpServletRequest request){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return HttpResult.createByErrorCodeMessage(HttpResultEnum.NEED_LOGIN.getCode(), "user need login in");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesConfig.getProperties("ftp.server.http.prefix") + targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return HttpResult.createBySuccess(fileMap);
        }else{
            return HttpResult.createByErrorMessage("need admin authority");
        }
    }

    @RequestMapping(value = "/uploadText", method = RequestMethod.POST)
    @ResponseBody
    public Map uploadImg(HttpSession session,
                         @RequestParam(value = "upload_file", required = false) MultipartFile file,
                         HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success", false);
            resultMap.put("msg", "please login in admin");
            return resultMap;
        }
        if (userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if (StringUtils.isBlank(targetFileName)) {
                resultMap.put("success", false);
                resultMap.put("msg", "upload image fail");
                return resultMap;
            }
            String url = PropertiesConfig.getProperties("ftp.server.http.prefix") + targetFileName;
            resultMap.put("success", true);
            resultMap.put("mgs", "upload image success");
            resultMap.put("file_path", url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        } else {
            resultMap.put("success", false);
            resultMap.put("msg", "need admin authority");
            return resultMap;
        }
    }
}