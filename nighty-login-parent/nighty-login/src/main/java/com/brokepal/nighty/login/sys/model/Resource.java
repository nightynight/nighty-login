package com.brokepal.nighty.login.sys.model;

import java.io.Serializable;

/**
 * Created by chenchao on 17/4/17.
 */
public class Resource implements Serializable {
    private static final long serialVersionUID = 3L;

    private String id;
    private String name;
    private String type;
    private String description;

    public Resource() {}

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Builder{
        private String id;
        private String name;
        private String type;
        private String description;

        public Builder id(String val){
            id = val;
            return this;
        }
        public Builder name(String val){
            name = val;
            return this;
        }
        public Builder type(String val){
            type = val;
            return this;
        }
        public Builder description(String val){
            description = val;
            return this;
        }
        public Resource build(){
            Resource resource = new Resource();
            resource.id = this.id;
            resource.name = this.name;
            resource.type = this.type;
            resource.description = this.description;
            return resource;
        }
    }
}
