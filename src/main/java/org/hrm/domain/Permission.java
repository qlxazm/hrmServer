package org.hrm.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Permission implements Serializable {
    private Integer id;
    private String permissionName;
    private String permissionNikeName;
    private Date createTime;
    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionNikeName() {
        return permissionNikeName;
    }

    public void setPermissionNikeName(String permissionNikeName) {
        this.permissionNikeName = permissionNikeName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
