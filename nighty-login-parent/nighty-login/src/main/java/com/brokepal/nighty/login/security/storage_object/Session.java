package com.brokepal.nighty.login.security.storage_object;

import java.io.Serializable;

/**
 * Created by chenchao on 17/4/11.
 */
public class Session implements Serializable {
    private static final long serialVersionUID = -201704112440000L;

    private String sessionId;
    private String token;


    public Session() {
    }

    public Session(String sessionId, String token) {
        this.sessionId = sessionId;
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
