package com.yum.ucp.modules.task.utils;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.constants.TaskConstants.ModuleActRowStatus;
import com.yum.ucp.common.utils.SnowFlakeUtils;
import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.task.dao.ModuleActRowDao;
import com.yum.ucp.modules.task.dao.ModuleActValueDao;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.entity.ModuleActValue;

public class ModuleActRowUtils {
	
	private static ModuleActRowDao moduleActRowDao = SpringContextHolder.getBean(ModuleActRowDao.class);
	private static ModuleActValueDao moduleActValueDao = SpringContextHolder.getBean(ModuleActValueDao.class);
	
	public static String getModuleActRowStatus(String abbr) {
		for(ModuleActRowStatus mar : ModuleActRowStatus.values()) {
			if(abbr.equals(mar.getAbbr())) {
				return mar.getValue();
			}
		}
		return ModuleActRowStatus.PENDING.value;
	}
	
	public static JSONArray findModuleRowArrayByActId(List<ModuleActRow> rowLists) {
        
        JSONArray rowArray = new JSONArray();
        if(rowLists != null && !rowLists.isEmpty()) {
        	for(ModuleActRow row : rowLists) {
        		JSONObject itemObject = new JSONObject();
        		itemObject.put("tempId", SnowFlakeUtils.getNextId());
        		itemObject.put("rowId", row.getId());
        		itemObject.put("configCertifiCenter", row.getConfigCertifiCenter());
        		itemObject.put("rowCheck", false);
        		List<ModuleActValue> valueLists = moduleActValueDao.findRowDataByRowIdAndColumnName(row.getId(), ModuleActValue.DEL_FLAG_NORMAL);
        		if(valueLists != null && !valueLists.isEmpty()) {
        			for(ModuleActValue mav : valueLists) {
        				itemObject.put(mav.getColumnName(), mav.getValue());
        			}
        		}
        		rowArray.add(itemObject);
        	}
        }
        return rowArray;
	}
}
