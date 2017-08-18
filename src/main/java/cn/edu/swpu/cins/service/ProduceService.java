package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Product;
import com.github.pagehelper.PageInfo;

public interface ProduceService {

    HttpResult saveOrUpdateProduct(Product product);

    HttpResult<String> setSaleStatus(Integer productId, Integer status);

    HttpResult<Object> getProductDetail(Integer productId);

    public HttpResult<PageInfo> getProductList(int pageNum, int pageSize);
}
