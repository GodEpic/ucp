/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.DscTaskLock;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 锁caseDAO接口
 *
 * @author Edward.Luo
 * @version 2016-12-12
 */
@MyBatisDao
public interface DscTaskLockDao extends CrudDao<DscTaskLock> {
    DscTaskLock getByCode(@Param("code") String code);

    void updateByCode(String key);

    void updateCodeTime(String time, String key);

    void deleteByCode(String key);

    void complainCode(String startTime, String endTime);

    void updateLockStatus(String key);

    void insertWorkTimeConfig(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("cron") String cron);

    void updateWorkTimeConfig(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("cron") String cron);

    Map<String, String> findWorkTimeConfig();
}