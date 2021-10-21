/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.func.entity;

import com.yum.ucp.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 对接功能明细Entity
 * @author Edward.Luo
 * @version 2016-12-22
 */
public class DscLinkFunction extends DataEntity<DscLinkFunction> {
	
	private static final long serialVersionUID = 1L;
	private String funCode;		// fun_code
	private String funTitle;		// fun_title
	private String funType;		// fun_type
	private String linkUrl;		// link_url
	private String linkParams;		// link_params
	private String linkImg;		// link_img
	private String repoParams;		// repo_params

	public DscLinkFunction() {
		super();
	}

	public DscLinkFunction(String id){
		super(id);
	}

	@Length(min=0, max=30, message="fun_code长度必须介于 0 和 30 之间")
	public String getFunCode() {
		return funCode;
	}

	public void setFunCode(String funCode) {
		this.funCode = funCode;
	}
	
	@Length(min=0, max=50, message="fun_title长度必须介于 0 和 50 之间")
	public String getFunTitle() {
		return funTitle;
	}

	public void setFunTitle(String funTitle) {
		this.funTitle = funTitle;
	}
	
	@Length(min=0, max=1, message="fun_type长度必须介于 0 和 1 之间")
	public String getFunType() {
		return funType;
	}

	public void setFunType(String funType) {
		this.funType = funType;
	}
	
	@NotNull(message="link_url不能为空")
	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	@Length(min=0, max=255, message="link_params长度必须介于 0 和 255 之间")
	public String getLinkParams() {
		return linkParams;
	}

	public void setLinkParams(String linkParams) {
		this.linkParams = linkParams;
	}
	
	@Length(min=0, max=100, message="link_img长度必须介于 0 和 100 之间")
	public String getLinkImg() {
		return linkImg;
	}

	public void setLinkImg(String linkImg) {
		this.linkImg = linkImg;
	}
	
	@Length(min=0, max=255, message="repo_params长度必须介于 0 和 255 之间")
	public String getRepoParams() {
		return repoParams;
	}

	public void setRepoParams(String repoParams) {
		this.repoParams = repoParams;
	}
	
}