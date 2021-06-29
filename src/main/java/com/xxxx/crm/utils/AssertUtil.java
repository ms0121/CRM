package com.xxxx.crm.utils;

import com.xxxx.crm.exceptions.ParamsException;

public class AssertUtil {


    /**
     * 用于判断传入的信息是否为真
     * @param flag
     * @param msg  如果为真，抛出异常信息
     */
    public static void isTrue(Boolean flag, String msg) {
        if (flag) {
            throw new ParamsException(msg);
        }
    }

}
