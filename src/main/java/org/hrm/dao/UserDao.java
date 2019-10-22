package org.hrm.dao;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.hrm.dao.provider.UserDynaSqlProvider;
import org.hrm.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param user
     * @return
     */
    @SelectProvider(type = UserDynaSqlProvider.class, method = "findUserByUserName")
    User findUserByUserName(User user);
}
