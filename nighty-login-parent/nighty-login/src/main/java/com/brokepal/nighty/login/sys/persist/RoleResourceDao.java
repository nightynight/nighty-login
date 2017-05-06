package com.brokepal.nighty.login.sys.persist;

import com.brokepal.nighty.login.sys.model.RoleResource;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface RoleResourceDao {
    List<RoleResource> findList(RoleResource roleResource);
}
