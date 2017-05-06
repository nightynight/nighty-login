package com.brokepal.nighty.login.sys.service;

import com.brokepal.nighty.login.sys.model.Role;
import com.brokepal.nighty.login.sys.persist.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by chenchao on 17/4/23.
 */
@Service
@Transactional(propagation= Propagation.REQUIRES_NEW,readOnly=false, timeout=3)
public class RoleService {
    @Autowired
    private RoleDao roleDao;

    public List<Role> getAll(){
        return roleDao.findList(new Role());
    }
}
