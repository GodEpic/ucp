/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.task.entity;

import com.yum.ucp.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 锁caseEntity
 * @author Edward.Luo
 * @version 2016-12-12
 */
public class DscTaskLock extends DataEntity<DscTaskLock> {
	
	private static final long serialVersionUID = 1L;
	private String taskCode;		// task_code
	private String lockStatus;		// lock_status
	private String taskType;		// task_type
	private String userName;		// userName

	public DscTaskLock() {
		super();
	}

	public DscTaskLock(String id){
		super(id);
	}

	@Length(min=0, max=100, message="task_code长度必须介于 0 和 100 之间")
	public String getTaskCode() {
		return taskCode;
	}

	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	
	@Length(min=0, max=1, message="lock_status长度必须介于 0 和 1 之间")
	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	
	@Length(min=0, max=100, message="task_type长度必须介于 0 和 100 之间")
	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}