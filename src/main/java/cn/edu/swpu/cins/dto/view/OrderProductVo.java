package cn.edu.swpu.cins.dto.view;

import cn.edu.swpu.cins.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public class OrderProductVo {
    private List<OrderItem> orderItemList;
    private BigDecimal productTotalPrice;
    private String imageHost;

    @Override
    public String toString() {
        return "OrderProductVo{" +
                "orderItemList=" + orderItemList +
                ", productTotalPrice=" + productTotalPrice +
                ", imageHost='" + imageHost + '\'' +
                '}';
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
