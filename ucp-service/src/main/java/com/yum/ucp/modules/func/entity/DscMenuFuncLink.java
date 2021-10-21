/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.entity;

import com.yum.ucp.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 菜单与功能关联关系Entity
 * @author Edward.Luo
 * @version 2016-12-22
 */
public class DscMenuFuncLink extends DataEntity<DscMenuFuncLink> {
	
	private static final long serialVersionUID = 1L;
	private String menuId;		// menu_id
	private String functionId;		// function_id
	
	public DscMenuFuncLink() {
		super();
	}

	public DscMenuFuncLink(String id){
		super(id);
	}

	@Length(min=0, max=32, message="menu_id长度必须介于 0 和 32 之间")
	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	@Length(min=0, max=32, message="function_id长度必须介于 0 和 32 之间")
	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	
}