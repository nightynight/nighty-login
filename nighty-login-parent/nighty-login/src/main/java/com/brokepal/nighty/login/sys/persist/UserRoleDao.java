package com.brokepal.nighty.login.sys.persist;


import com.brokepal.nighty.login.sys.model.UserRole;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface UserRoleDao {
    List<UserRole> findList(UserRole userRole);
    int createRoleUser(UserRole userRole);
}
