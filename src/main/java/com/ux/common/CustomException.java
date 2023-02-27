package com.ux.common;

/**
 * 自定义业务异常类
 * @author john
 * @version 1.1
 */

public class CustomException extends RuntimeException{
    /**
     * 异常信息
     * @param message
     */
    public CustomException(String message) {
        super(message);
    }
}
