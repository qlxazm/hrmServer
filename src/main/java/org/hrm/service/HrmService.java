package org.hrm.service;


import org.hrm.domain.Dept;
import org.hrm.domain.Permission;

import java.util.List;

public interface HrmService {
    /**
     * 根据id查询出dept信息
     * @param dept
     * @return
     */
    Dept findDeptById(Dept dept);

    /**
     * 获取所有的权限
     * @return
     */
    List<Permission> getAllPermissions();
}
