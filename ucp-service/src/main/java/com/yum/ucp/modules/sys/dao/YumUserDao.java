/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.sys.entity.YumUser;

/**
 * 用户DAO接口
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface YumUserDao extends CrudDao<YumUser> {

    /**
     * 根据登录名称查询用户
     *
     * @param userNo 工号
     * @return YumUser
     */
    public YumUser getByUserNo(String userNo);
}
