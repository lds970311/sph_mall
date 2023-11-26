package com.evan.mall.handler;


import com.evan.mall.common.execption.GmallException;
import com.evan.mall.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> error(Exception e) {
        log.error("全局异常处理器捕获到异常：{}", e.getMessage());
        return Result.fail(e.getMessage());
    }

    /**
     * 自定义异常处理方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(GmallException.class)
    @ResponseBody
    public Result<String> error(GmallException e) {
        return Result.fail(e.getMessage());
    }
}

