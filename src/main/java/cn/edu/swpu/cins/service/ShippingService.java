package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Shipping;
import com.github.pagehelper.PageInfo;

public interface ShippingService {

    HttpResult addAddress(Integer userId, Shipping shipping);

    HttpResult<String> deleteAddress(Integer userId, Integer shippingId);

    HttpResult updateAddress(Integer userId, Shipping shipping);

    HttpResult<Shipping> selectAddress(Integer userId, Integer shippingId);

    HttpResult<PageInfo> getList(Integer userId, Integer pageNum, Integer pageSize);
}
