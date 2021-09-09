package com.scistor.estools.base;

import com.scistor.estools.constant.Constant;

import java.io.Serializable;

/**
 * 
 * @author tianlin
 *
 */
public class HttpRespBody<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static int OK = 1;
    private static int ERROR = 0;
    private static int UNAUTHORIZED = 401;

    private int code;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public HttpRespBody(int code, T data) {
        super();
        this.code = code;
        this.data = data;
    }

    public static HttpRespBody<String> okBody() {
        return okBody("ok");
    }

    public static <T> HttpRespBody<T> okBody(T data) {
        HttpRespBody<T> httpRespBody = new HttpRespBody<>(OK, data);
        return httpRespBody;
    }

    public static <T> HttpRespBody<T> unauthorizedBody(T data) {
        HttpRespBody<T> httpRespBody = new HttpRespBody<>(UNAUTHORIZED, data);
        return httpRespBody;
    }

    public static HttpRespBody<String> errorBody() {
        return errorBody(Constant.SERVER_ERROR);
    }

    public static <T> HttpRespBody<T> errorBody(T data) {
        HttpRespBody<T> httpRespBody = new HttpRespBody<>(ERROR, data);
        return httpRespBody;
    }
}
