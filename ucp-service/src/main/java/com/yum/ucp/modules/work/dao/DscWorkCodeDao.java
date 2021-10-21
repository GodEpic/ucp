/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.work.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.work.entity.DscWorkCode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工号DAO接口
 * @author Edward.Luo
 * @version 2017-01-19
 */
@MyBatisDao
public interface DscWorkCodeDao extends CrudDao<DscWorkCode> {
    List<DscWorkCode> findByUserId(@Param("userId")String userId,@Param("roleType")String roleType);

    List<String> findRoleNamesByCode(@Param("workCode")String workCode);

    List<String> findRoleAnnounceByCodeRoleType(@Param("roleType")String roleType,@Param("roleName") String roleName);

    List<Dict> findRoleTypesByUserId(@Param("userId")String userId);

    void bindWorkCodeRole(@Param("roleId")String roleId,@Param("workCodeId")String workCodeId);

    void deleteWorkCodeRole(@Param("workCodeId")String workCodeId);

    List<DscWorkCode> getUserWorkCode(@Param("userId")String userId);

    long getUserWorkCodeCount(@Param("userId")String userId);

    void bindUserWorkCode(@Param("userId")String userId,@Param("workCodeId")String workCodeId);

    DscWorkCode getByWorkCode(@Param("workCode")String workCode);

    /**
     * 依据角色类型获取用户id
     * @param roleTypes
     * @return
     */
    List<DscWorkCode> findUserIdsByRoleType(@Param("roleTypes") String[] roleTypes);
}