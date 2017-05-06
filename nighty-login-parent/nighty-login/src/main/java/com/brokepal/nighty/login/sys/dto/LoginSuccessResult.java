package com.brokepal.nighty.login.sys.dto;

import com.brokepal.nighty.login.sys.model.Resource;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenchao on 17/4/12.
 */
public class LoginSuccessResult implements Serializable {
    private static final long serialVersionUID = -201704122355000L;

    private String token;
    private String nickname;
    private String username;
    private String roleType; //角色类型 只能是"admin"或"user"，用来控制前台页面跳转
    private List<Resource> resources; //用户权限

    public LoginSuccessResult() {}

    public LoginSuccessResult(String token, String nickname, String username, String roleType, List<Resource> resources) {
        this.token = token;
        this.nickname = nickname;
        this.username = username;
        this.roleType = roleType;
        this.resources = resources;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
