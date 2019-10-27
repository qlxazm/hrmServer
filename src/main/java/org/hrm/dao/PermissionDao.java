package org.hrm.dao;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import static org.hrm.utils.HrmConstants.SECURITY_PERMISSION;
import org.hrm.domain.Permission;

import java.util.List;

public interface PermissionDao {
    /**
     * 获取所有的权限
     * @return
     */
    @Select("SELECT id, permission, name FROM " + SECURITY_PERMISSION)
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "permission", property = "permission"),
            @Result(column = "name", property = "name"),
            @Result(column = "id", property = "roles",
            many = @Many(select = "org.hrm.dao.RoleDao.findRolesByPermissionId"))
    })
    List<Permission> getAllPermission();
}
