/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.func.entity.DscMenuFuncLink;
import com.yum.ucp.modules.sys.dao.MenuDao;
import com.yum.ucp.modules.sys.entity.Menu;
import com.yum.ucp.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class MenuService extends CrudService<MenuDao, Menu> {
	/**
	 * 根据menuId查询接口
	 * @param menuId
	 */
	public List<String> findLinkFunctionIds(String menuId){
		return  dao.findLinkFunctionIds(menuId);
	}
	/**
	 * 删除case与接口模块的关系
	 * @param menuId
	 */
	public void deleteBatchMenuLinkByMenuId(String menuId){
		dao.deleteBatchMenuLinkByMenuId(menuId);
	}
	/**
	 * 批量case与接口模块的关系
	 * @param dscMenuFuncLinks
	 */
	public void  insertBatchMenuLinkByMenuId(List<DscMenuFuncLink> dscMenuFuncLinks){
		dao.insertBatchMenuLinkByMenuId(dscMenuFuncLinks);
	}
	/**
	 * 查询case类别
	 * @param parentId
	 * @return
	 */
	public List<Menu> findMenusByParentId(String parentId,int type){
		return dao.findMenusByParentId(parentId,type);
	}

	/**
	 *根据issuetypeId查询paojrectKey
	 * @param issueTypeId
	 * @return
	 */
	public String  getProjectKey(String issueTypeId){
		return dao.getProjectKey(issueTypeId);
	}

	/**
	 * 根据parentIds
	 * @param parentIds
	 * @return
	 */
	public List<Menu> findMenusByParentIds(String parentIds,Integer type){
		return dao.findMenusByParentIds(parentIds,type);
	}
	/**
	 *根据工号查询业务
	 * @param workCode
	 * @return
	 */
	public List<Menu> findByWorkCode(String workCode,String userId){
		return dao.findByWorkCode(workCode,userId);
	}

	/**
	 * 查询业务类型
	 * @return
	 */
	public List<Menu> findMenus(){
		return UserUtils.findMenus();
	}
	/**
	 * 根据热线查询菜单
	 * @return
	 */
	public List<Menu> findMenusByHotLine(String hotLine){
		return UserUtils.findMenusByHotLine(hotLine);
	}
	/**
	 * 查询子业务类型
	 * @return
	 */
	public List<Menu> findChildrenMenus(){
		return UserUtils.findChildrenMenus();
	}

	/**
	 * 查询子业务类型
	 * @return
	 */
	public List<Menu> findAllMenus(){
		return UserUtils.findAllMenus();
	}
}
