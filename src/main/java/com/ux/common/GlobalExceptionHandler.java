package com.ux.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 * @author john
 * @version 1.1
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    /**
     * 新增管理重名问题
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        log.error(exception.getMessage());
        // 1、搜索异常信息中是否存在 Duplicate entry 如果存在 说明就是 用户名重复导致的错误
        if (exception.getMessage().contains("Duplicate entry")){
            // 2、 然后将异常信息进行拆分，获得重复的用户名
            String[] split = exception.getMessage().split(" ");
            // 3、将获得的用户名和提示信息扔进R.error 传回前台
            String msg = split[2] + "已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    /**
     * 菜品，套餐关联异常处理
     * @param exception
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){
        return R.error(exception.getMessage());
    }
}
