package com.yum.ucp.modules.task.utils;

import java.util.List;

import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.task.dao.ModuleActValueDao;
import com.yum.ucp.modules.task.entity.ModuleActValue;

public class ModuleActValueUtils {

	private static ModuleActValueDao moduleActValueDao = SpringContextHolder.getBean(ModuleActValueDao.class);
	
	public static List<ModuleActValue> findRowDataByModIdAndRowId(String moduleId, String rowId) {
		return moduleActValueDao.findRowDataByModIdAndRowId(moduleId, rowId);
	}
	
	
	public static List<ModuleActValue> findRowDataByRowId(String rowId) {
		return moduleActValueDao.findRowDataByRowId(rowId, ModuleActValue.DEL_FLAG_NORMAL);
	}
}
