package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.response.HttpResult;
import cn.edu.swpu.cins.entity.Product;

public interface ProduceService {

    HttpResult saveOrUpdateProduct(Product product);

    HttpResult<String> setSaleStatus(Integer productId, Integer status);

    HttpResult<Object> getProductDetail(Integer productId);
}
