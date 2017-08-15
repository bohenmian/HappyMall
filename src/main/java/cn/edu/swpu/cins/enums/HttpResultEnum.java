package cn.edu.swpu.cins.enums;

public enum HttpResultEnum {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(3, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(4, "ILLEGAL_ARGUMENT");

    private int code;
    private String descrption;

    HttpResultEnum(int code, String descrption) {
        this.code = code;
        this.descrption = descrption;
    }

    public int getCode() {
        return code;
    }

    public String getDescrption() {
        return descrption;
    }
}
