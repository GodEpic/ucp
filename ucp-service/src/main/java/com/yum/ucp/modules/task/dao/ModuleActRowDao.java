/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.ModuleActRow;

/**
 * 导入模块行DAO接口
 * @author tony
 * @version 2019-07-24
 */
@MyBatisDao
public interface ModuleActRowDao extends CrudDao<ModuleActRow> {
	
	/**
	 * 
	 * @param actId
	 * @param value
	 * @param version
	 * @return
	 */
	public ModuleActRow getByActIdAndValueAndVersion(@Param("actId") String actId, @Param("value") String value, @Param("version") Integer version);
	
	/**
	 * 
	 * @param actId
	 * @param version
	 */
	public void deleteByActIdAndVersion(@Param("actId") String actId, @Param("version") Integer version);
	
	/**
	 * 
	 * @param row
	 * @return
	 */
	public List<ModuleActRow> findListByActIdAndVersion(ModuleActRow row);

	Integer countByActIdAndVersion(ModuleActRow row);
	
	Integer getModuleRowLastVersionByActId(@Param("actId") String actId, @Param("delFlag") String delFlag);

	List<ModuleActRow> findModuleRowQaResult(@Param("actId") String actId, @Param("checkVersion") Integer checkVersion, @Param("rowVersion") Integer rowVersion, @Param("delFlag") String delFlag);
}