/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.func.entity.DscLinkFunction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 对接功能明细DAO接口
 * @author Edward.Luo
 * @version 2016-12-22
 */
@MyBatisDao
public interface DscLinkFunctionDao extends CrudDao<DscLinkFunction> {

    List<DscLinkFunction> findListByType(String type);

    List<DscLinkFunction> findListByMenuId(@Param("menuIds") List<String>  menuIds);
}