package org.hrm.domain;

import java.io.Serializable;

public class Role implements Serializable {
    private Integer id;
    private String roleName;
    private String roleNikeName;

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
