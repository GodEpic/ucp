/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.senderlog.dao;


import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.senderlog.entity.DscSMSEmailLog;

/**
 * case配置DAO接口
 * @author Edward.Luo
 * @version 2016-12-12
 */
@MyBatisDao
public interface DscSMSEmailLogDao extends CrudDao<DscSMSEmailLog> {

}