package com.brokepal.nighty.login.security.constant;

/**
 * Created by chenchao on 17/3/30.
 */
public class SecurityConstant {
    private SecurityConstant(){}

    public static final int TRY_COUNT = 3; //尝试次数，尝试那么多次失败后锁定账号
    public static final int LOCK_TIME = 10; //锁定时间，单位：分

    public static final String LAST_LOGIN_PRE = "lastLogin";
    public static final String LOGIN_SUCCESS_PRE = "loginSuccess";
    public static final String LOGIN_FAIL_PRE = "loginFail";
    public static final String PRIVATE_KEY_PRE = "privateKey";
}
