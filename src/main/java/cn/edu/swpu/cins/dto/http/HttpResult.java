package cn.edu.swpu.cins.dto.http;

import cn.edu.swpu.cins.enums.HttpResultEnum;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class HttpResult<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    public HttpResult() {
    }

    public HttpResult(int status) {
        this.status = status;
    }

    public HttpResult(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public HttpResult(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public HttpResult(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.status == HttpResultEnum.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> HttpResult<T> createBySuccess() {
        return new HttpResult<T>(HttpResultEnum.SUCCESS.getCode());
    }

    public static <T> HttpResult<T> createBySuccessMessage(String msg) {
        return new HttpResult<T>(HttpResultEnum.SUCCESS.getCode(), msg);
    }


    public static <T> HttpResult<T> createBySuccess(T data) {
        return new HttpResult<T>(HttpResultEnum.SUCCESS.getCode(), data);
    }

    public static <T> HttpResult<T> createBySuccess(String msg, T data) {
        return new HttpResult<T>(HttpResultEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> HttpResult<T> createByError() {
        return new HttpResult<T>(HttpResultEnum.ERROR.getCode(), HttpResultEnum.ERROR.getDescrption());
    }

    public static <T> HttpResult<T> createByErrorMessage(String errorMessage) {
        return new HttpResult<T>(HttpResultEnum.ERROR.getCode(), errorMessage);
    }

    public static <T> HttpResult<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new HttpResult<T>(errorCode, errorMessage);
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
