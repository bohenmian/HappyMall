package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.http.HttpResult;

import java.util.Map;

public interface OrderService {

    HttpResult pay(Long orderNo, Integer userId, String path);

    HttpResult alipayCallback(Map<String, String> params);

    HttpResult queryOrderPayStatus(Integer userId, Long orderNo);

    HttpResult createOrder(Integer userId, Integer shippingId);

    HttpResult<String> cancelOrder(Integer userId, Long orderNo);

}
