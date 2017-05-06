package com.brokepal.nighty.login.sys.persist;

import com.brokepal.nighty.login.sys.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public interface UserDao {
    int createUser(User user);
    int updatePassword(@Param("email") String email, @Param("password") String password);
    int updateLastLoginTime(@Param("username") String username, @Param("lastLoginTime") Date lastLoginTime);
    User getUserByUsername(String username);
    User getUserByEmail(String email);


    /******************************** 以下为管理员对应的业务方法 ************************************/

    public List<User> findList(User filterEntity);
}
