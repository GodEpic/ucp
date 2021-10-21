/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.entity;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * jira用户Entity
 * @author Edward.Luo
 * @version 2017-01-19
 */
public class DscHotLineConfig extends DataEntity<DscHotLineConfig> {

	private static final long serialVersionUID = 1L;
	private String name;
	private String href;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private String type
			;
	private Boolean  isChecked;

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}