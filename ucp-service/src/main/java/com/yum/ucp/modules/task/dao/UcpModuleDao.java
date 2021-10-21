/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.UcpModule;
import com.yum.ucp.modules.task.pojo.UcpModuleVO;

/**
 * 导入模块DAO接口
 * @author tony
 * @version 2019-07-24
 */
@MyBatisDao
public interface UcpModuleDao extends CrudDao<UcpModule> {
	/**
	 * 根据所属系统和模块名称查询
	 * @param sysId
	 * @param modName
	 * @return
	 */
	UcpModule getBySysAndName(@Param("sysId") String sysId, @Param("name") String name);
	
	/**
	 * 
	 * @param actId
	 * @param version
	 * @return
	 */
	List<UcpModule> findByActIdAndRowIdAndVersion(@Param("actId") String actId, @Param("rowId") String rowId, @Param("version") Integer version);
	
	/**
	 * 查询活动下系统信息
	 * @param actId
	 * @param version
	 * @param delFlag
	 * @return
	 */
	List<UcpModuleVO> findSysByActIdAndVersion(@Param("actId") String actId, @Param("version") Integer version, @Param("delFlag") String delFlag);
	
	/**
	 * 查询活动下模块信息
	 * @param actId
	 * @param version
	 * @param delFlag
	 * @return
	 */
	List<UcpModuleVO> findModByActIdAndVersion(@Param("actId") String actId, @Param("version") Integer version, @Param("delFlag") String delFlag);
}