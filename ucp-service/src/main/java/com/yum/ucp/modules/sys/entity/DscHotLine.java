/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.entity;

import com.yum.ucp.common.persistence.DataEntity;

import java.util.List;

/**
 * jira用户Entity
 * @author Edward.Luo
 * @version 2017-01-19
 */
public class DscHotLine extends DataEntity<DscHotLine> {

	private static final long serialVersionUID = 1L;
	private String phoneNum;
	private String menuId;
	private List<String> menuIds;
	private List<String> configIds;

	public List<String> getConfigIds() {
		return configIds;
	}

	public void setConfigIds(List<String> configIds) {
		this.configIds = configIds;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public List<String> getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(List<String> menuIds) {
		this.menuIds = menuIds;
	}
}