package com.brokepal.nighty.login.core.util;

import java.util.Random;

/**
 * Created by Administrator on 2016/11/10.
 */
public class CommonUtil {
    /**
     * 生成随机字符串
     * @param count 需要几位的随机字符串
     * @return
     */
    public static String createRandomString(int count){
        String codes = "0123456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ";
        StringBuilder result = new StringBuilder();
        Random r = new Random();
        for (int i=0; i < count; i++) {
            int index = r.nextInt(codes.length());
            result.append(codes.charAt(index));
        }
        return result.toString();
    }
}
