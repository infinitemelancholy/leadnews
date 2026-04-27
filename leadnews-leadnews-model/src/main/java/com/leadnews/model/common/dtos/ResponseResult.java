package com.leadnews.model.common.dtos;

import com.alibaba.fastjson.JSON;
import com.leadnews.model.common.enums.AppHttpCodeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 閫氱敤鐨勭粨鏋滆繑鍥炵被
 * @param <T>
 */
public class ResponseResult<T> implements Serializable {

    private String host;

    private Integer code;

    private String errorMessage;

    private T data;

    public ResponseResult() {
        this.code = 200;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.errorMessage = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
    }

    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }

    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }

    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getErrorMessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getErrorMessage());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums, String errorMessage){
        return setAppHttpCodeEnum(enums,errorMessage);
    }

    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getCode(),enums.getErrorMessage());
    }

    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage){
        return okResult(enums.getCode(),errorMessage);
    }

    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.errorMessage = msg;
        return this;
    }

    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public static void main(String[] args) {
        //鍓嶇疆
        /*AppHttpCodeEnum success = AppHttpCodeEnum.SUCCESS;
        System.out.println(success.getCode());
        System.out.println(success.getErrorMessage());*/

        //鏌ヨ涓€涓璞?
        /*Map map = new HashMap();
        map.put("name","zhangsan");
        map.put("age",18);
        ResponseResult result = ResponseResult.okResult(map);
        System.out.println(JSON.toJSONString(result));*/


        //鏂板锛屼慨鏀癸紝鍒犻櫎  鍦ㄩ」鐩腑缁熶竴杩斿洖鎴愬姛鍗冲彲
        /*ResponseResult result = ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        System.out.println(JSON.toJSONString(result));*/


        //鏍规嵁涓嶇敤鐨勪笟鍔¤繑鍥炰笉鍚岀殑鎻愮ず淇℃伅  姣斿锛氬綋鍓嶆搷浣滈渶瑕佺櫥褰曘€佸弬鏁伴敊璇?
        /*ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN,"鑷畾涔夋彁绀轰俊鎭?);
        System.out.println(JSON.toJSONString(result));*/

        //鏌ヨ鍒嗛〉淇℃伅
        PageResponseResult responseResult = new PageResponseResult(1,5,50);
        List list = new ArrayList();
        list.add("itcast");
        list.add("leadnews");
        responseResult.setData(list);
        System.out.println(JSON.toJSONString(responseResult));

    }

}

