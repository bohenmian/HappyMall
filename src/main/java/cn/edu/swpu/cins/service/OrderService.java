package cn.edu.swpu.cins.service;

import cn.edu.swpu.cins.dto.http.HttpResult;

public interface OrderService {

    HttpResult pay(Long orderNo, Integer userId, String path);
}
