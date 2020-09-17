package com.tsbg.mis.serviceImpl.baseUtils;

/**
 * @author 汪永晖
 */
public class IdentifyCodeUtil {

    public static String getRandom() {
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            num = num.append(String.valueOf((int) Math.floor(Math.random() * 9 + 1)));
        }
        return num.toString();
    }
}
