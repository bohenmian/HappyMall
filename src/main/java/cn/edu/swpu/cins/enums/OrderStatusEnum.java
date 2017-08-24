package cn.edu.swpu.cins.enums;

import cn.edu.swpu.cins.exception.HappyMallException;

public enum OrderStatusEnum {
    CANCELED(0,"已取消"),
    NO_PAY(10,"未支付"),
    PAID(20,"已付款"),
    SHIPPED(40,"已发货"),
    ORDER_SUCCESS(50,"订单完成"),
    ORDER_CLOSE(60,"订单关闭");

    private int code;
    private String value;

    OrderStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static OrderStatusEnum codeOf(int code){
        for(OrderStatusEnum orderStatusEnum : values()){
            if(orderStatusEnum.getCode() == code){
                return orderStatusEnum;
            }
        }
        throw new HappyMallException("not found enum");
    }
}
