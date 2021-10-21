package com.yum.ucp.modules.enumerate;

import java.util.Arrays;

public enum NotifyActivityStatus {
    wait("1","待接受"),
    accept("2","接受"),
    mkt_revocation("3","mkt已撤销"),
    mkt_reject("4","mkt已退回"),
    finance_revocation("5","财务已撤销"),
    finance_reject("6","财务已退回"),
    legal_revocation("7","法务已撤销"),
    legal_reject("8","法务已退回"),
    ucp_reject("9","配置系统退回"),
    WAITING_MKT_CONFIRM("10", "待MKT确认","WAITING_MKT_CONFIRM"),
    WAITING_FINANCE_CONFIRM ("11","待财务确认","WAITING_FINANCE_CONFIRM"),
    FINANCE_CONFIRM("12","财务已确认","FINANCE_CONFIRM"),
    JOIN("13","会审","JOIN"),
    WAITING_ECOM_FINANCE_CONFIRM("14","待电商财务确认","WAITING_ECOM_FINANCE_CONFIRM"),
    ECOM_FINANCE_CONFIRM("15","电商财务已确认","ECOM_FINANCE_CONFIRM"),
    WAITING_DR_FINANCE_CONFIRM("16","待餐厅财务确认","WAITING_DR_FINANCE_CONFIRM"),
    DR_FINANCE_CONFIRM("17","餐厅财务确认","DR_FINANCE_CONFIRM"),
    
    //WAITING_


            ;

    private String code;
    private String desc;
    private String outCode;
    NotifyActivityStatus(String code,String desc){
        this.code=code;
        this.desc=desc;
    }
    NotifyActivityStatus(String code,String desc,String outCode){
        this.code=code;
        this.desc=desc;
        this.outCode=outCode;
    }

    public static String getDescByStatus(String status) {
        for (NotifyActivityStatus notifyActivityStatus:NotifyActivityStatus.values()){
            if(notifyActivityStatus.code.equals(status)){
                return notifyActivityStatus.desc;
            }
        }
        return "";
    }


    public static String getStatusByOutCode(String outCode) {
        for (NotifyActivityStatus notifyActivityStatus:NotifyActivityStatus.values()){
            if(outCode.equals(notifyActivityStatus.outCode)){
                return notifyActivityStatus.code;
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getOutCode() {
        return outCode;
    }

    /**
     * 判断是否可以撤回
     * @param code
     * @return
     */
    public static boolean isReject(String code){
       return Arrays.asList(WAITING_MKT_CONFIRM.getCode(),WAITING_FINANCE_CONFIRM.getCode(),JOIN.getCode(),WAITING_ECOM_FINANCE_CONFIRM.getCode(),NotifyActivityStatus.accept.getCode()).contains(code);

    }


}

