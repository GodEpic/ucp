/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.entity;

import com.yum.ucp.common.persistence.DataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class YumUser extends DataEntity<YumUser> {
    private static final long serialVersionUID = 1L;

    private String id;

    private String userName;

    private String userNo;

    private String status;

    private String roleType;

    private String jira;

    private String jiraNo;

    private Date createTime;

    private String creator;

    private String loginPass;

    private String loginType;

    private String notifyId;

}