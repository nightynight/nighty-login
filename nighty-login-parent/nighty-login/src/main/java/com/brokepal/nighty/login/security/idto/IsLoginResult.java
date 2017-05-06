package com.brokepal.nighty.login.security.idto;

/**
 * Created by chenchao on 17/5/4.
 */
public class IsLoginResult {
    private boolean isLogin;
    private String message;


    public static IsLoginResult buildTrueInstance(){
        IsLoginResult isLoginResult = new IsLoginResult();
        isLoginResult.isLogin = true;
        return isLoginResult;
    }

    public static IsLoginResult buildFalseInstance(String message){
        IsLoginResult isLoginResult = new IsLoginResult();
        isLoginResult.isLogin = false;
        isLoginResult.message = message;
        return isLoginResult;
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
