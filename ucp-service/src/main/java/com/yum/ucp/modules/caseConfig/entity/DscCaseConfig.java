/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.caseConfig.entity;

import com.yum.ucp.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;


/**
 * case配置Entity
 * @author Edward.Luo
 * @version 2016-12-12
 */
public class DscCaseConfig extends DataEntity<DscCaseConfig> {
	
	private static final long serialVersionUID = 1L;
	private String caseCode;		// case_code
	private String viewPage;		// view_page
	private String lv1Page;		// lv1_page
	private String lv2Page;		// lv2_page
	private String caseType;		// case_type
	private String projectId;		// case_type

	public DscCaseConfig() {
		super();
	}

	public DscCaseConfig(String id){
		super(id);
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Length(min=0, max=100, message="case_code长度必须介于 0 和 100 之间")
	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	
	@Length(min=0, max=100, message="view_page长度必须介于 0 和 100 之间")
	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
	@Length(min=0, max=100, message="lv1_page长度必须介于 0 和 100 之间")
	public String getLv1Page() {
		return lv1Page;
	}

	public void setLv1Page(String lv1Page) {
		this.lv1Page = lv1Page;
	}
	
	@Length(min=0, max=100, message="lv2_page长度必须介于 0 和 100 之间")
	public String getLv2Page() {
		return lv2Page;
	}

	public void setLv2Page(String lv2Page) {
		this.lv2Page = lv2Page;
	}
	
	@Length(min=0, max=1, message="case_type长度必须介于 0 和 1 之间")
	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}
	
}