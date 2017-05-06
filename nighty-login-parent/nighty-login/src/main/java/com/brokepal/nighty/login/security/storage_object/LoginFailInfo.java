package com.brokepal.nighty.login.security.storage_object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenchao on 17/3/30.
 */

/**
 * 登录失败时缓存中需要存的对象
 * key为username
 * value为该对象
 */
public class LoginFailInfo implements Serializable {
    private static final long serialVersionUID = -201703302146000L;

    //登录失败时需要记录的信息
    private int failCount;
    private Date lastLockTime;

    public LoginFailInfo() {}

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public Date getLastLockTime() {
        return lastLockTime;
    }

    public void setLastLockTime(Date lastLockTime) {
        this.lastLockTime = lastLockTime;
    }

}
