/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.sys.entity.DscHotLineConfig;

import java.util.List;

/**
 * jira用户DAO接口
 * @author Edward.Luo
 * @version 2017-01-19
 */
@MyBatisDao
public interface DscHotLineConfigDao extends CrudDao<DscHotLineConfig> {

   List<String> findListByHotLine(String hotLine);

}