package org.hrm.dao.provider;

import org.apache.ibatis.jdbc.SQL;
import org.hrm.domain.User;
import static org.hrm.utils.HrmConstants.USERTABLE;

public class UserDynaSqlProvider {
    /**
     * 根据用户名查询用户信息
     * @param user
     * @return
     */
    public String findUserByUserName(User user) {
        String sql = new SQL(){
            {
                SELECT("username, password");
                FROM(USERTABLE);
                WHERE("username = #{username}");
            }
        }.toString();
        return sql;
    }
}
