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
public class DscConfigLine extends DataEntity<DscConfigLine> {

	private static final long serialVersionUID = 1L;
	private String hotLineId;
	private String configId;

	public String getHotLineId() {
		return hotLineId;
	}

	public void setHotLineId(String hotLineId) {
		this.hotLineId = hotLineId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}
}