package cn.edu.swpu.cins.controller.portal;


import cn.edu.swpu.cins.dto.request.ProductDetail;
import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/getDetail", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult<ProductDetail> getDetail(Integer productId) {
        return productService.getProductDetail(productId);
    }
}
