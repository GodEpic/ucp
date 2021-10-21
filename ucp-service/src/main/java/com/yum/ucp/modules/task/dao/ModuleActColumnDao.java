/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.ModuleActColumn;

/**
 * 导入模块列DAO接口
 * @author tony
 * @version 2019-07-24
 */
@MyBatisDao
public interface ModuleActColumnDao extends CrudDao<ModuleActColumn> {
	
	/**
	 * 根据活动ID和列英文名以及版本查询
	 * @param actId
	 * @param name
	 * @param version
	 * @return
	 */
	public ModuleActColumn getByActIdAndNameAndVersion(@Param("actId") String actId, @Param("name") String name, @Param("version") Integer version);
	
	/**
	 * 根据活动ID和版本号删除
	 * @param actId
	 * @param version
	 */
	public void deleteByActIdAndVersion(@Param("actId") String actId, @Param("version") Integer version);
	
	/**
	 * 根据活动ID查询所有列信息
	 * @param moduleId
	 * @param rowId
	 * @return
	 */
	public List<ModuleActColumn> findListByActIdAndVersion(@Param("actId") String actId, @Param("version") Integer version, @Param("delFlag") String delFlag);
}