package com.brokepal.nighty.login.security.util;

import com.brokepal.nighty.login.core.util.MD5Util;

/**
 * Created by chenchao on 17/3/29.
 */
public final class SecurityUtil {
    public static String generateToken(String username, String password){
        return MD5Util.string2MD5(username + " " + password);
    }
}
