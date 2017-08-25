package cn.edu.swpu.cins.enums;

import cn.edu.swpu.cins.exception.HappyMallException;

public enum PaymentTypeEnum {

    ONLINE_PAY(1,"在线支付");
    private int code;
    private String value;

    PaymentTypeEnum(int code, String value) {
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

    public static PaymentTypeEnum codeOf(int code){
        for(PaymentTypeEnum paymentTypeEnum : values()){
            if(paymentTypeEnum.getCode() == code){
                return paymentTypeEnum;
            }
        }
        throw new HappyMallException("not found payment type enum");
    }
}
