/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.caseConfig.dao;


import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.caseConfig.entity.DscCaseConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * case配置DAO接口
 * @author Edward.Luo
 * @version 2016-12-12
 */
@MyBatisDao
public interface DscCaseConfigDao extends CrudDao<DscCaseConfig> {
	List<DscCaseConfig> findListByType(@Param("caseType") String caseType);
}