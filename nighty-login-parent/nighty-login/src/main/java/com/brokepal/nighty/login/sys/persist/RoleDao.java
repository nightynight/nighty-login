package com.brokepal.nighty.login.sys.persist;


import com.brokepal.nighty.login.sys.model.Role;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface RoleDao {
    Role get(String id);
    List<Role> findList(Role role);

}
