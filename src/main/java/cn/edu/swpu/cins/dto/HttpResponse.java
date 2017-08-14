package cn.edu.swpu.cins.dto;

import java.io.Serializable;

public class HttpResponse<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    public HttpResponse() {
    }

    public HttpResponse(int status) {
        this.status = status;
    }

    public HttpResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public HttpResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public HttpResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
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

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}
