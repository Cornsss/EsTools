package com.scistor.estools.base;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;

/**
 * 定义统一返回的JSON视图
 * 
 * @author tianlin
 *
 */
public class ViewResponse extends ResponseEntity<HttpRespBody<?>> {
    public ViewResponse(HttpStatus status) {
        super(status);
    }

    public ViewResponse(@Nullable HttpRespBody<?> body, HttpStatus status) {
        super(body, status);
    }

    public ViewResponse(MultiValueMap<String, String> headers, HttpStatus status) {
        super(headers, status);
    }

    public ViewResponse(@Nullable HttpRespBody<?> body, @Nullable MultiValueMap<String, String> headers,
                        HttpStatus status) {
        super(body, headers, status);
    }

    /**
     * 指定一个错误码(符合http规范的statu code)和消息体
     * 
     * @param <T>
     * @param <T>
     * @param status
     * @param body
     * @return
     */
    public static <T> ViewResponse result(HttpStatus status, T body) {
        HttpRespBody<?> httpRespBody = HttpRespBody.okBody(body);
        ViewResponse res = new ViewResponse(httpRespBody, status);
        return res;
    }

    /**
     * 使用默认的HTPP status(200)和自定义的消息体
     * 
     * @param <T>
     * @param <T>
     * @param body
     * @return
     */
    public static <T> ViewResponse success(T body) {
        HttpRespBody<?> httpRespBody = HttpRespBody.okBody(body);
        ViewResponse res = new ViewResponse(httpRespBody, HttpStatus.OK);
        return res;
    }

    /**
     * 使用默认的使用默认的HTPP status(200)和默认成功的消息体{"code":1,"body":"ok"}
     * 
     * @return
     */
    public static ViewResponse success() {
        ViewResponse res = new ViewResponse(HttpRespBody.okBody(), HttpStatus.OK);
        return res;
    }

    /**
     * 使用默认的使用默认的HTPP status(200)和默认失败的消息体{"code":0,"body":"error"}
     * 
     * @return
     */
    public static ViewResponse failed() {
        ViewResponse res = new ViewResponse(HttpRespBody.errorBody(), HttpStatus.OK);
        return res;
    }

    public static <T> ViewResponse failed(T body) {
        HttpRespBody<?> httpRespBody = HttpRespBody.errorBody(body);
        ViewResponse res = new ViewResponse(httpRespBody, HttpStatus.OK);
        return res;
    }

    public static <T> ViewResponse unauthorized(T body) {
        HttpRespBody<?> httpRespBody = HttpRespBody.unauthorizedBody(body);
        ViewResponse res = new ViewResponse(httpRespBody, HttpStatus.OK);
        return res;
    }

}
