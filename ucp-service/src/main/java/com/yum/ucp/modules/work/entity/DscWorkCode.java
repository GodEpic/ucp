/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.work.entity;

import com.google.common.collect.Lists;
import com.yum.ucp.common.persistence.DataEntity;
import com.yum.ucp.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 工号Entity
 * @author Edward.Luo
 * @version 2017-01-19
 */
public class DscWorkCode extends DataEntity<DscWorkCode> {
	
	private static final long serialVersionUID = 1L;
	private String workCode;		// work_code
	private String roleType;		// roleType
	private User user;		// user_id
	private  String  roleName;		// roleName

	public List<String> roleIdList = Lists.newArrayList();

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public DscWorkCode() {
		super();
	}

	public DscWorkCode(String id){
		super(id);
	}

	@Length(min=1, max=100, message="work_code长度必须介于 1 和 100 之间")
	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}