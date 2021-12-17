/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.sys.dao.YumUserDao;
import com.yum.ucp.modules.sys.entity.YumUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Yun用户信息
 *
 * @author Chevy.Yang
 */
@Service
@Transactional(readOnly = true)
public class YumUserService extends CrudService<YumUserDao, YumUser> {

    @Autowired
    private YumUserDao yumUserDao;

    public YumUser getByUserNo(String userNo) {
        return yumUserDao.getByUserNo(userNo);
    }

}
