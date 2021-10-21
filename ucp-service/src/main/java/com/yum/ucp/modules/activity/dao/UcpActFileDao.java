/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.UcpActFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 特殊活动附件关联DAO接口
 * @author cherlin
 * @version 2019-08-23
 */
@MyBatisDao
public interface UcpActFileDao extends CrudDao<UcpActFile> {

    List<UcpActFile> findByActId(@Param("actId") String actId);
}