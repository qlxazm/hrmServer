package org.hrm.domain;

import java.io.Serializable;
import java.util.Date;

public class Role implements Serializable {
    private Integer id;
    private String roleName;
    private String roleNikeName;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleNikeName() {
        return roleNikeName;
    }

    public void setRoleNikeName(String roleNikeName) {
        this.roleNikeName = roleNikeName;
    }
}
