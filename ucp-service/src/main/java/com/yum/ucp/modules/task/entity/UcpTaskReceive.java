/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * 任务认领Entity
 * @author tony
 * @version 2019-08-06
 */
public class UcpTaskReceive extends DataEntity<UcpTaskReceive> {
	
	private static final long serialVersionUID = 1L;
	private String taskId;		// 任务ID
	private String receive;		// 认领人
	private String jiraStatus;		// jira节点状态
	private Date receiveTime;		// 认领时间
	private Date finishTime;		// 完成时间
	
	public UcpTaskReceive() {
		super();
	}

	public UcpTaskReceive(String id){
		super(id);
	}

	@Length(min=0, max=32, message="任务ID长度必须介于 0 和 32 之间")
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	@Length(min=0, max=100, message="认领人长度必须介于 0 和 100 之间")
	public String getReceive() {
		return receive;
	}

	public void setReceive(String receive) {
		this.receive = receive;
	}
	
	@Length(min=0, max=100, message="jira节点状态长度必须介于 0 和 10 之间")
	public String getJiraStatus() {
		return jiraStatus;
	}

	public void setJiraStatus(String jiraStatus) {
		this.jiraStatus = jiraStatus;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	
}