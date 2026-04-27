package com.leadnews.common.exception;


import com.leadnews.model.common.dtos.ResponseResult;
import com.leadnews.model.common.enums.AppHttpCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice  //鎺у埗鍣ㄥ寮虹被
@Slf4j
public class ExceptionCatch {

    /**
     * 澶勭悊涓嶅彲鎺у紓甯?
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        e.printStackTrace();
        log.error("catch exception:{}",e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**
     * 澶勭悊鍙帶寮傚父  鑷畾涔夊紓甯?
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult exception(CustomException e){
        log.error("catch exception:{}",e);
        return ResponseResult.errorResult(e.getAppHttpCodeEnum());
    }
}

