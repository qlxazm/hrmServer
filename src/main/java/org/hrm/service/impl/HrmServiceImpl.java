package org.hrm.service.impl;

import org.hrm.dao.DeptDao;
import org.hrm.dao.PermissionDao;
import org.hrm.domain.Dept;
import org.hrm.domain.Permission;
import org.hrm.service.HrmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 使用事务数据库的默认的事务隔离级别
 */
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
@Service("hrmService")
public class HrmServiceImpl implements HrmService {

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> getAllPermissions() {
        return permissionDao.getAllPermission();
    }
}
