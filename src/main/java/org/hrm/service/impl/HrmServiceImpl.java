package org.hrm.service.impl;

import org.hrm.dao.DeptDao;
import org.hrm.domain.Dept;
import org.hrm.service.HrmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 使用事务数据库的默认的事务隔离级别
 */
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
@Service("hrmService")
public class HrmServiceImpl implements HrmService {

    @Autowired
    private DeptDao deptDao;

    @Override
    public Dept findDeptById(Dept dept) {
        dept = deptDao.selectById(dept.getId());
        return dept;
    }
}
