/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.work.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.work.entity.DscRoleWorkCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工号DAO接口
 * @author Edward.Luo
 * @version 2017-01-19
 */
@MyBatisDao
public interface DscRoleWorkCodeDao extends CrudDao<DscRoleWorkCode> {

    public DscRoleWorkCode checkIfExist(@Param("roleId") String roleId, @Param("workCodeId") String workCodeId);

    public List<DscRoleWorkCode> findByWorkCodeId(@Param("workCodeId") String workCodeId);

    public void deleteById(@Param("workCodeId") String workCodeId);

}