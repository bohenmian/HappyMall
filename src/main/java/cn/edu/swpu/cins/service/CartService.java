package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.dto.view.CartVo;

public interface CartService {

    HttpResult<CartVo> add(Integer userId, Integer productId, Integer count);
}
