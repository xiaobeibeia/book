package com.tsbg.mis.util;

import java.util.Random;

/**
 * 密码盐生成
 *
 * @author 汪永晖
 */

public class SaltUtil {

    public static String saltGenerator() {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int nu = random.nextInt(6);
            sb.append(str.charAt(nu));
        }
        return sb.toString();
    }
}
