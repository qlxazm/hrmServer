package org.hrm.dao.provider;

import org.apache.ibatis.jdbc.SQL;
import org.hrm.domain.User;

import static org.hrm.utils.HrmConstants.*;

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
                FROM("`" + SECURITY_ROLE + "` as sr, `" + SECURITY_USER_ROLE + "` as sur, `" + USERTABLE + "` as u");
                WHERE("u.id = sur.userid");
                AND();
                WHERE(" sr.id = sur.roleid");
                AND();
                WHERE(" u.username = #{username}");
            }

            /*SELECT roleName,roleNikeName FROM `security_role` as sr, `security_user_role` as sur, `user_inf` as u
            WHERE u.id = sur.userid AND sr.id = sur.roleid AND u.username = '超级管理员'*/
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
                FROM(SECURITY_PERMISSION + " as sp, " + SECURITY_ROLE_PERMISSION + " as srp, " + SECURITY_ROLE + " as sr");
                WHERE(" sp.id = srp.permissionId ");
                AND();
                WHERE(" sr.id = srp.roleId ");
                AND();
                WHERE(" sp.id = #{permissionId}");
            }
        }.toString();
        return sql;
    }
}
