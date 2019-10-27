package org.hrm.dao;

import org.apache.ibatis.annotations.SelectProvider;
import org.hrm.dao.provider.RoleDynaSqlProvider;
import org.hrm.domain.Role;
import org.hrm.domain.User;

import java.util.List;

public interface RoleDao {

    /**
     * 根据用户名查询用户角色
     * @return
     */
    @SelectProvider(type = RoleDynaSqlProvider.class, method = "findRolesByUserName")
    List<Role> findRolesByUserName(User user);

    /**
     * 根据权限id查询该权限对应的角色
     * @param id
     * @return
     */
    @SelectProvider(type = RoleDynaSqlProvider.class, method = "findRolesByPermissionId")
    List<Role> findRolesByPermissionId(Integer id);
}
