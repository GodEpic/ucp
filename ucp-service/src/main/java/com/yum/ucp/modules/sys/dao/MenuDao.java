/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.func.entity.DscMenuFuncLink;
import com.yum.ucp.modules.sys.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单DAO接口
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface MenuDao extends CrudDao<Menu> {
    List<String> findLinkFunctionIds(String menuId);

    void deleteBatchMenuLinkByMenuId(String menuId);

    void insertBatchMenuLinkByMenuId(@Param("dscMenuFuncLinks") List<DscMenuFuncLink> dscMenuFuncLinks);

    List<Menu> findMenusByParentId(@Param("parentId") String parentId, @Param("type") int type);

    String getProjectKey(@Param("issueTypeId") String issueTypeId);

    List<Menu> findMenusByParentIds(@Param("parentIds") String parentIds, @Param("type") Integer type);

//    List<Menu> findMenusByType(@Param("type") int type);

    List<Menu> findMenusByRemarks(@Param("remarks") String remarks);

    List<Menu> findMenus();

    List<Menu> findMenusByHotLine(String hotLine);

    List<Menu> findChildrenMenus();

    List<Menu> findByWorkCode(@Param("workCode") String workCode, @Param("userId") String userId);

    List<Menu> findList();

    List<Menu> findByUserId(Menu menu);

    int updateParentIds(Menu menu);

    int updateSort(Menu menu);

}
