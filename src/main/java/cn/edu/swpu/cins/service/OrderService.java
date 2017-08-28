package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.dto.view.OrderVo;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface OrderService {

    HttpResult pay(Long orderNo, Integer userId, String path);

    HttpResult alipayCallback(Map<String, String> params);

    HttpResult queryOrderPayStatus(Integer userId, Long orderNo);

    HttpResult createOrder(Integer userId, Integer shippingId);

    HttpResult<String> cancelOrder(Integer userId, Long orderNo);

    HttpResult getOrderProduct(Integer userId);

    HttpResult<OrderVo> getOrderDetail(Integer userId, Long orderNo);

    HttpResult<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize);

    HttpResult<PageInfo> getList(int pageNum, int pageSize);

    HttpResult<OrderVo> getDetail(Long orderNo);

    HttpResult<PageInfo> searchOrder(Long orderNo, int pageNum, int pageSize);

}
