package cn.edu.swpu.cins.service.impl;

import cn.edu.swpu.cins.config.BigDecimalConfig;
import cn.edu.swpu.cins.config.DateTimeDeserializer;
import cn.edu.swpu.cins.config.FtpConfig;
import cn.edu.swpu.cins.config.PropertiesConfig;
import cn.edu.swpu.cins.dao.*;
import cn.edu.swpu.cins.dto.http.Const;
import cn.edu.swpu.cins.dto.http.HttpResult;
import cn.edu.swpu.cins.dto.view.OrderItemVo;
import cn.edu.swpu.cins.dto.view.OrderProductVo;
import cn.edu.swpu.cins.dto.view.OrderVo;
import cn.edu.swpu.cins.dto.view.ShippingVo;
import cn.edu.swpu.cins.entity.*;
import cn.edu.swpu.cins.enums.OrderStatusEnum;
import cn.edu.swpu.cins.enums.PayPlatformEnum;
import cn.edu.swpu.cins.enums.PaymentTypeEnum;
import cn.edu.swpu.cins.enums.ProductStatusEnum;
import cn.edu.swpu.cins.exception.OrderNotExitedException;
import cn.edu.swpu.cins.service.OrderService;
import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;
    private PayInfoMapper payInfoMapper;
    private CartMapper cartMapper;
    private ProductMapper productMapper;
    private ShippingMapper shippingMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderItemMapper orderItemMapper,
                            PayInfoMapper payInfoMapper, CartMapper cartMapper,
                            ProductMapper productMapper, ShippingMapper shippingMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.payInfoMapper = payInfoMapper;
        this.cartMapper = cartMapper;
        this.productMapper = productMapper;
        this.shippingMapper = shippingMapper;
    }

    @Transactional(rollbackFor = {DataAccessException.class})
    public HttpResult createOrder(Integer userId, Integer shippingId) {
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        HttpResult httpResult = this.getCartItem(userId, cartList);
        if (!httpResult.isSuccess()) {
            return httpResult;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) httpResult.getData();
        BigDecimal payment = this.getOrderTotalPrice(orderItemList);
        Order order = this.assembleOrder(userId, shippingId, payment);
        if (order == null) {
            return HttpResult.createByErrorMessage("create order fail");
        }
        if (CollectionUtils.isEmpty(orderItemList)) {
            return HttpResult.createByErrorMessage("cart is null");
        }
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
        }
        orderItemMapper.bactchInsert(orderItemList);
        this.reduceProductStock(orderItemList);
        this.cleanCart(cartList);
        OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
        return HttpResult.createBySuccess(orderVo);
    }

    private HttpResult<List<OrderItem>> getCartItem(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(cartList)) {
            return HttpResult.createByErrorMessage("cart is null");
        }
        for (Cart cartItem : cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (ProductStatusEnum.ON_SALE.getCode() != product.getStatus()) {
                return HttpResult.createByErrorMessage(product.getName() + "not on sale");
            }
            if (cartItem.getQuantity() > product.getStock()) {
                return HttpResult.createByErrorMessage(product.getName() + "shortage of stock");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalConfig.mul(product.getPrice().doubleValue(), cartItem.getQuantity()));
            orderItemList.add(orderItem);
        }
        return HttpResult.createBySuccess(orderItemList);
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalConfig.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        long orderNo = this.generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        int rowCount = orderMapper.insert(order);
        if (rowCount > 0) {
            return order;
        }
        return null;
    }

    private long generateOrderNo() {
        long currentTime = System.currentTimeMillis();
        return currentTime + new Random().nextInt(100);
    }

    private void reduceProductStock(List<OrderItem> orderItemList) {
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productMapper.updateByPrimaryKey(product);
        }
    }

    private void cleanCart(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
    }

    private OrderVo assembleOrderVo(Order order, List<OrderItem> orderItemList) {
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentType(order.getPaymentType());
        orderVo.setPaymentTypeDesc("在线支付");
        orderVo.setPaymentTypeDesc(PaymentTypeEnum.codeOf(order.getPaymentType()).getValue());
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(OrderStatusEnum.codeOf(order.getStatus()).getValue());
        orderVo.setShippingId(order.getShippingId());
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping != null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(this.assembleShippingVo(shipping));
        }
        orderVo.setPaymentTime(DateTimeDeserializer.dateToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeDeserializer.dateToStr(order.getSendTime()));
        orderVo.setEndTime(DateTimeDeserializer.dateToStr(order.getEndTime()));
        orderVo.setCreateTime(DateTimeDeserializer.dateToStr(order.getCreateTime()));
        orderVo.setCloseTime(DateTimeDeserializer.dateToStr(order.getCloseTime()));
        orderVo.setImageHost(PropertiesConfig.getProperties("ftp.server.http.prefix"));
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        for (OrderItem orderItem : orderItemList) {
            OrderItemVo orderItemVo = this.assembleOrderItemVo(orderItem);
            orderItemVoList.add(orderItemVo);
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        return orderVo;
    }

    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(DateTimeDeserializer.dateToStr(orderItem.getCreateTime()));
        return orderItemVo;
    }

    private ShippingVo assembleShippingVo(Shipping shipping) {
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        shippingVo.setReceiverPhone(shippingVo.getReceiverPhone());
        return shippingVo;
    }

    public HttpResult pay(Long orderNo, Integer userId, String path) {
        Map<String, String> resultMap = Maps.newHashMap();
       Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new OrderNotExitedException("order not exited");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));
        String outTradeNo = order.getOrderNo().toString();
        String subject = new StringBuilder().append("happymall扫码支付,订单号:").append(outTradeNo).toString();
        String totalAmount = order.getPayment().toString();
        String undiscountableAmount = "0";
        String sellerId = "";
        String body = new StringBuilder().append("订单").append(outTradeNo).append("购买商品共").append(totalAmount).append("元").toString();
        String operatorId = "test_operator_id";
        String storeId = "test_store_id";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");
        String timeoutExpress = "120m";
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
        for (OrderItem orderItem : orderItemList) {
            GoodsDetail goods = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalConfig.mul(orderItem.getCurrentUnitPrice().doubleValue(), new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods);

        }
        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesConfig.getProperties("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        Configs.init("zfbinfo.properties");
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                logger.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                File file = new File(path);
                if (!file.exists()) {
                    file.setWritable(true);
                    file.mkdirs();
                }
                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/" + "qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                File targetFile = new File(path, qrFileName);
                try {
                    FtpConfig.uploadFile(Lists.newArrayList(targetFile));
                } catch (IOException e) {
                    logger.error("upload qr code error");
                }
                logger.info("qrPath:" + qrPath);
                String qrUrl = PropertiesConfig.getProperties("ftp.server.http.prefix") + targetFile;
                resultMap.put("qrUrl", qrUrl);
                return HttpResult.createBySuccess(resultMap);
            case FAILED:
                logger.error("支付宝预下单失败!!!");
                return HttpResult.createByErrorMessage("支付宝预下单失败!!!");
            case UNKNOWN:
                logger.error("系统异常，预下单状态未知!!!");
                return HttpResult.createByErrorMessage("系统异常，预下单状态未知!!!");
            default:
                logger.error("不支持的交易状态，交易返回异常!!!");
                return HttpResult.createByErrorMessage("不支持的交易状态，交易返回异常!!!");
        }
    }

    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            logger.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                logger.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            logger.info("body:" + response.getBody());
        }
    }


    public HttpResult alipayCallback(Map<String, String> params) {
        Long orderNo = Long.parseLong(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new OrderNotExitedException("not happymall order, callback ignore");
        }
        if (order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return HttpResult.createBySuccess("alipay repeat request");
        }
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setPaymentTime(DateTimeDeserializer.stringToDate(params.get("gmt_payment")));
            order.setStatus(OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKeySelective(order);
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(orderNo);
        payInfo.setPayPlatform(PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);
        payInfoMapper.insert(payInfo);
        return HttpResult.createBySuccess();
    }

    public HttpResult queryOrderPayStatus(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new OrderNotExitedException("not happymall order");
        }
        if (order.getStatus() >= OrderStatusEnum.PAID.getCode()) {
            return HttpResult.createBySuccess();
        }
        return HttpResult.createByError();
    }

    public HttpResult<String> cancelOrder(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order == null) {
            throw new OrderNotExitedException("order not exited");
        }
        if (order.getStatus() != OrderStatusEnum.NO_PAY.getCode()) {
            throw new OrderNotExitedException("order has pay");
        }
        Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setStatus(OrderStatusEnum.CANCELED.getCode());
        int row = orderMapper.updateByPrimaryKeySelective(updateOrder);
        if (row > 0) {
            return HttpResult.createBySuccess();
        }
        return HttpResult.createByError();
    }

    public HttpResult getOrderProduct(Integer userId) {
        OrderProductVo orderProductVo = new OrderProductVo();
        List<Cart> cartList = cartMapper.selectCheckedByUserId(userId);
        HttpResult result = this.getCartItem(userId, cartList);
        if (!result.isSuccess()) {
            return result;
        }
        List<OrderItem> orderItemList = (List<OrderItem>) result.getData();
        List<OrderItemVo> orderItemVoList = Lists.newArrayList();
        BigDecimal payment = new BigDecimal("0");
        for (OrderItem orderItem : orderItemList) {
            payment = BigDecimalConfig.add(payment.doubleValue(), orderItem.getTotalPrice().doubleValue());
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderProductVo.setProductTotalPrice(payment);
        orderProductVo.setOrderItemList(orderItemList);
        orderProductVo.setImageHost(PropertiesConfig.getProperties("ftp.server.http.prefix"));
        return HttpResult.createBySuccess(orderProductVo);
    }

    public HttpResult<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        Order order = orderMapper.selectByUserIdAndOrderNo(userId, orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(orderNo, userId);
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            return HttpResult.createBySuccess(orderVo);
        }
        throw new OrderNotExitedException("order not exit");
    }

    public HttpResult<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.getList(userId);
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, userId);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return HttpResult.createBySuccess(pageInfo);
    }

    private List<OrderVo> assembleOrderVoList(List<Order> orderList, Integer userId) {
        List<OrderVo> orderVoList = Lists.newArrayList();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = Lists.newArrayList();
            if (userId == null) {
                orderItemList = orderItemMapper.getByOrderNo(order.getOrderNo());
            } else {
                orderItemList = orderItemMapper.getByOrderNoUserId(order.getOrderNo(), userId);
            }
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    public HttpResult<PageInfo> getList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.getAll();
        List<OrderVo> orderVoList = this.assembleOrderVoList(orderList, null);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return HttpResult.createBySuccess(pageInfo);
    }

    public HttpResult<OrderVo> getDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            return HttpResult.createBySuccess(orderVo);
        } else {
            throw new OrderNotExitedException("order not exit");
        }
    }

    public HttpResult<PageInfo> searchOrder(Long orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order != null) {
            List<OrderItem> orderItemList = orderItemMapper.getByOrderNo(orderNo);
            OrderVo orderVo = this.assembleOrderVo(order, orderItemList);
            PageInfo pageInfo = new PageInfo(Lists.newArrayList(order));
            pageInfo.setList(Lists.newArrayList(orderVo));
            return HttpResult.createBySuccess(pageInfo);
        }
        throw new OrderNotExitedException("order not exit");
    }
}
