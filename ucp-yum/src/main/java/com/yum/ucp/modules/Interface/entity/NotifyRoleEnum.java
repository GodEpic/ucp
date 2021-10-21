package com.yum.ucp.modules.Interface.entity;

public enum NotifyRoleEnum {
    COUPON_ACTIVITY_APPLY_ROLE(1,"活动申请角色","COUPON_ACTIVITY_APPLY_ROLE"),
    COUPON_FINANCE_AUDIT_ROLE(2,"财务审核角色","COUPON_FINANCE_AUDIT_ROLE"),
    COUPON_DR_FINANCE_AUDIT_ROLE(4,"餐厅财务审批角色","COUPON_DR_FINANCE_AUDIT_ROLE"),
    COUPON_B2_FINANCE_AUDIT_ROLE(5,"B2模板电商审批角色","COUPON_B2_FINANCE_AUDIT_ROLE"),
    COUPON_B1_FINANCE_AUDIT_ROLE(6,"B1模板电商审批角色","COUPON_B1_FINANCE_AUDIT_ROLE"),
    COUPON_CSSC_FINANCE_AUDIT_ROLE(7,"CSSC审批角色","COUPON_CSSC_FINANCE_AUDIT_ROLE"),
    COUPON_PRIME_FINANCE_AUDIT_ROLE(8,"券码审系统财务Prime审批模板","COUPON_PRIME_FINANCE_AUDIT_ROLE"),
    COUPON_VFC_B1_FINANCE_AUDIT_ROLE(9,"券码审系统VFC审批模板B1","COUPON_VFC_B1_FINANCE_AUDIT_ROLE"),
    COUPON_VFC_B2_FINANCE_AUDIT_ROLE(10,"券码审系统VFC审批模板B2","COUPON_VFC_B2_FINANCE_AUDIT_ROLE"),
    COUPON_LEGAL_AUDIT_ROLE(3,"法务审核角色","COUPON_LEGAL_AUDIT_ROLE");

    private int num;
    private String desc;
    private String code;

    NotifyRoleEnum(int num,String desc,String code){
        this.code=code;
        this.num=num;
        this.desc=desc;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
