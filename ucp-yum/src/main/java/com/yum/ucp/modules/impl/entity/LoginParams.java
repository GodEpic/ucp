package com.yum.ucp.modules.impl.entity;

/**
 * Created by Administrator on 2019/2/19.
 */
public class LoginParams{

    private String username;
    private String workCode;
    private String roleType;
    private String roleName;

    private String key;//关键词
    private String issueTypeId;//Case类型
    private String status;//状态
    private String priority;//优先级
    private String createCaseUrl;//创建Case的URL

    public String getCreateCaseUrl() {
        return createCaseUrl;
    }

    public void setCreateCaseUrl(String createCaseUrl) {
        this.createCaseUrl = createCaseUrl;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIssueTypeId() {
        return issueTypeId;
    }

    public void setIssueTypeId(String issueTypeId) {
        this.issueTypeId = issueTypeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
