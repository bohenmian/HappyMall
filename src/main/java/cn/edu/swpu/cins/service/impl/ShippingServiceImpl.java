package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.dao.ShippingMapper;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.entity.Shipping;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ShippingServiceImpl {

    private ShippingMapper shippingMapper;

    @Autowired
    public ShippingServiceImpl(ShippingMapper shippingMapper) {
        this.shippingMapper = shippingMapper;
    }

    public HttpResult addAddress(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if (rowCount > 0) {
            Map map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return HttpResult.createBySuccess("add address success", map);
        }
        return HttpResult.createByErrorMessage("add address fail");
    }


}
