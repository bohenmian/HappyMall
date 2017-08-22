package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.dto.view.CartVo;

public interface CartService {

    HttpResult<CartVo> add(Integer userId, Integer productId, Integer count);

    HttpResult<CartVo> updateCart(Integer userId, Integer productId, Integer count);

    HttpResult<CartVo> deleteProduct(Integer userId, String productIds);

    HttpResult<CartVo> getList(Integer userId);

    HttpResult<CartVo> selectAll(Integer userId, Integer productId, Integer checked);

    HttpResult<Integer> getProductCount(Integer userId);

}
