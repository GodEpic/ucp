/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.entity;

import com.yum.ucp.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * jira用户Entity
 * @author Edward.Luo
 * @version 2017-01-19
 */
public class DscUserJira extends DataEntity<DscUserJira> {
	
	private static final long serialVersionUID = 1L;
	private String loginName;		// login_name
	private String name;		// name
	private String password;		// password
	
	public DscUserJira() {
		super();
	}

	public DscUserJira(String id){
		super(id);
	}

	@Length(min=1, max=100, message="login_name长度必须介于 0 和 100 之间")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Length(min=1, max=100, message="name长度必须介于 1 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=30, message="password长度必须介于 1 和 30 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}