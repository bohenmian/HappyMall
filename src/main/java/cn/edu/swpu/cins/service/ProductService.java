package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.view.ProductDetail;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import com.github.pagehelper.PageInfo;

public interface ProductService {

    HttpResult saveOrUpdateProduct(Product product);

    HttpResult<String> setSaleStatus(Integer productId, Integer status);

    HttpResult<ProductDetail> manageProductDetail(Integer productId);

    HttpResult<PageInfo> getProductList(int pageNum, int pageSize);

    HttpResult<PageInfo> searchProduct(String productName, Integer prodcutId, int pageNum, int pageSize);

    HttpResult<ProductDetail> getProductDetail(Integer productId);

    HttpResult<PageInfo> getProductByKeyword(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
}
