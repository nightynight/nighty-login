package com.brokepal.nighty.login.sys.model;

/**
 * Created by chenchao on 17/4/17.
 */
public class RoleResource {
    private static final long serialVersionUID = 4L;

    private String id;
    private String roleId;
    private String resourceId;

    public RoleResource() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public static class Builder{
        private String id;
        private String roleId;
        private String resourceId;

        public Builder id(String val){
            id = val;
            return this;
        }
        public Builder roleId(String val){
            roleId = val;
            return this;
        }
        public Builder resourceId(String val){
            resourceId = val;
            return this;
        }
        public RoleResource build(){
            RoleResource roleResource = new RoleResource();
            roleResource.id = this.id;
            roleResource.roleId = this.roleId;
            roleResource.resourceId = this.resourceId;
            return roleResource;
        }
    }
}
