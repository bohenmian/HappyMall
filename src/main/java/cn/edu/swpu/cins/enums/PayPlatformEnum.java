package cn.edu.swpu.cins.enums;

public enum PayPlatformEnum {
    ALIPAY(1,"支付宝");
    private int code;
    private String value;

    PayPlatformEnum(int code, String value) {
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
}
