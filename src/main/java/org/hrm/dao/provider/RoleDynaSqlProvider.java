package org.hrm.dao.provider;

import org.apache.ibatis.jdbc.SQL;
import org.hrm.domain.User;

import static org.hrm.utils.HrmConstants.ROLE;
import static org.hrm.utils.HrmConstants.USERTABLE;
import static org.hrm.utils.HrmConstants.USER_ROLE;
import static org.hrm.utils.HrmConstants.PERMISSION;
import static org.hrm.utils.HrmConstants.ROLE_PERMISSION;

public class RoleDynaSqlProvider {

    /**
     * 根据用户名查询用户的角色信息
     * @param user
     * @return
     */
    public String findRolesByUserName(User user) {
        String sql = new SQL(){
            {
                SELECT("roleName, roleNikeName");
                FROM("`" + ROLE + "` as r, `" + USER_ROLE + "` as ur, `" + USERTABLE + "` as u");
                WHERE("u.id = ur.userId");
                AND();
                WHERE(" r.id = ur.roleId");
                AND();
                WHERE(" u.username = #{username}");
            }

        }.toString();
        return sql;
    }

    /**
     * 根据权限id查询出该权限对应的角色
     * @param permissionId
     * @return
     */
    public String findRolesByPermissionId(Integer permissionId) {
        String sql = new SQL(){
            {
                SELECT("roleName, roleNikeName");
                FROM(PERMISSION + " as p, " + ROLE_PERMISSION + " as rp, " + ROLE + " as r");
                WHERE(" p.id = rp.permissionId ");
                AND();
                WHERE(" r.id = rp.roleId ");
                AND();
                WHERE(" p.id = #{permissionId}");
            }
        }.toString();
        return sql;
    }
}
