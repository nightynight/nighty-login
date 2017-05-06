package com.brokepal.nighty.login.sys.persist;


import com.brokepal.nighty.login.sys.model.Resource;

import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface ResourceDao {
    Resource get(String id);
    List<Resource> findList(Resource resource);
}
