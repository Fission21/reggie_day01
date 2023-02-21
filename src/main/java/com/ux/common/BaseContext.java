package com.ux.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户Id
 * @author john
 * @version 1.1
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 保存id
     * @param id
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     * 取得id
     * @return
     */
    public static Long getCurrent(){
        return threadLocal.get();
    }
}
