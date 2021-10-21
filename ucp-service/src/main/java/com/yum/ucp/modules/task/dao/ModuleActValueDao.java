/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.ModuleActValue;

/**
 * 导入模块值DAO接口
 * @author tony
 * @version 2019-07-24
 */
@MyBatisDao
public interface ModuleActValueDao extends CrudDao<ModuleActValue> {
	
	/**
	 * 根据列ID和行ID查询字段值
	 * @param columnId
	 * @param rowId
	 * @return
	 */
	public ModuleActValue getByColumnIdAndRowId(@Param("columnId") String columnId, @Param("rowId") String rowId);
	
	/**
	 * 
	 * @param actId
	 * @param version
	 */
	public void deleteByActIdAndVersion(@Param("actId") String actId, @Param("version") Integer version);
	
	
	/**
	 *根据字段名和行的唯一字段值 查询第一次版本的值
	 * @param actId
	 * @param name
	 * @param version
	 * @return
	 */
	public ModuleActValue getFirstVersionValue(@Param("columnName") String columnName, @Param("rowValue") String rowValue, @Param("actId") String actId);
	
	/**
	 * 根据行ID查询是否有变更的记录
	 * @param rowId
	 * @return
	 */
	public Integer countByRowIdAndChange(@Param("rowId") String rowId, @Param("delFlag") String delFlag);
	
	/**
	 * 根据模块查询行字段值
	 * @param moduleId
	 * @param rowId
	 * @return
	 */
	public List<ModuleActValue> findRowDataByModIdAndRowId(@Param("moduleId") String moduleId, @Param("rowId") String rowId);
	
	/**
	 * 根据模块查询行值
	 * @param rowId
	 * @return
	 */
	public List<ModuleActValue> findRowDataByRowId(@Param("rowId") String rowId, @Param("delFlag") String delFlag);
	
	/**
	 * 根据行ID和列名查询列值
	 * @param rowId
	 * @param delFlag
	 * @return
	 */
	List<ModuleActValue> findRowDataByRowIdAndColumnName(@Param("rowId") String rowId, @Param("delFlag") String delFlag);

	/**
	 * 导出测试任务
	 * @param rowId
	 * @param version
	 * @param delFlag
	 * @return
	 */
	List<ModuleActValue> exportRowDataByActIdAndRowId(@Param("actId") String actId, @Param("rowId") String rowId, @Param("version") Integer version, @Param("delFlag") String delFlag);
}