package org.hrm.service;


import org.hrm.domain.Dept;

public interface HrmService {
    /**
     * 根据id查询出dept信息
     * @param dept
     * @return
     */
    Dept findDeptById(Dept dept);
}
