package com.hzk.util;

import org.springframework.stereotype.Component;

/**
 * 创建一个Threadlocal的工具类，用于保存和获取当前用户的id
 */

public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    //提供构造方法
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
       return threadLocal.get();
    }
}
