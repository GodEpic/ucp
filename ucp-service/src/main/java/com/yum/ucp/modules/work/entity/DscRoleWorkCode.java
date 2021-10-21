package com.yum.ucp.modules.work.entity;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * Created by Administrator on 2017/2/15.
 */
public class DscRoleWorkCode extends DataEntity<DscRoleWorkCode> {

    private String roleId;

    private String workCodeId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getWorkCodeId() {
        return workCodeId;
    }

    public void setWorkCodeId(String workCodeId) {
        this.workCodeId = workCodeId;
    }
}
