/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.func.entity.DscMenuFuncLink;

/**
 * 菜单与功能关联关系DAO接口
 * @author Edward.Luo
 * @version 2016-12-22
 */
@MyBatisDao
public interface DscMenuFuncLinkDao extends CrudDao<DscMenuFuncLink> {
	
}