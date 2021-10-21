package com.yum.ucp.modules.task.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.constants.ActivityImportConstants;
import com.yum.ucp.common.constants.TaskConstants.ModuleActRowStatus;
import com.yum.ucp.common.exception.ResponseErrorCode;
import com.yum.ucp.common.utils.*;
import com.yum.ucp.modules.sys.dao.DictDao;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.utils.DictUtils;
import com.yum.ucp.modules.task.dao.ModuleActColumnDao;
import com.yum.ucp.modules.task.dao.ModuleActRowDao;
import com.yum.ucp.modules.task.dao.ModuleActValueDao;
import com.yum.ucp.modules.task.dao.UcpModuleDao;
import com.yum.ucp.modules.task.entity.ModuleActColumn;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.entity.ModuleActValue;
import com.yum.ucp.modules.task.entity.UcpModule;

public class ActivityHandleDataUtils {

	private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ActivityHandleDataUtils.class);
	private static UcpModuleDao ucpModuleDao = SpringContextHolder.getBean(UcpModuleDao.class);
	private static ModuleActColumnDao moduleActColumnDao = SpringContextHolder.getBean(ModuleActColumnDao.class);
	private static ModuleActRowDao moduleActRowDao = SpringContextHolder.getBean(ModuleActRowDao.class);
	private static ModuleActValueDao moduleActValueDao = SpringContextHolder.getBean(ModuleActValueDao.class);
	private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
	
	public JSONObject handleImportData(JSONObject result, String actId, Integer version) {
		JSONObject handleResult = new JSONObject();
		
		handleResult.put("errcode", ResponseErrorCode.SUCCESS.getCode());
		handleResult.put("errmsg", ResponseErrorCode.SUCCESS.getDesc());
		
		try {
			ActivityHandleDataUtils utils = new ActivityHandleDataUtils();
			Map<Integer, ModuleActRow> rowMap = new HashMap<Integer, ModuleActRow>();
			if(result != null && !result.isEmpty()) {
				
				deletePrevVesionData(actId, version);
				
				JSONArray dataArray = result.getJSONArray("data");

				JSONObject rowData = result.getJSONObject("rowData");
				JSONArray firstRowDataArray = rowData.getJSONArray("dataValue");

				for (int j = 0; j < firstRowDataArray.size(); j++) {
					JSONObject rowValueObject = firstRowDataArray.getJSONObject(j);
					Integer rowIndex = rowValueObject.getInteger("rowIndex");
					String rowValue = rowValueObject.getString("rowValue");
					rowMap.put(rowIndex, utils.saveMasterData(rowValue, actId, version, rowIndex));
				}

				int dataSize = dataArray.size();
				int runSize = 10;
				int pageSize = dataSize/runSize;

				JedisUtils.set("thread_" + actId, "0", 300);

				CountDownLatch countDown = new CountDownLatch(runSize);
				ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(runSize);
				for(int pageNum = 1; pageNum <= runSize; pageNum++){
					int startIndex = (pageNum - 1) * pageSize;
					int endIndex = pageNum * pageSize;
					if(pageNum == runSize){
						endIndex = dataArray.size();
					}
					ExecupteHelp hpRunnable = new ExecupteHelp(dataArray, startIndex, endIndex, actId, version, rowMap, countDown);
					executor.execute(hpRunnable);
				}
				executor.shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
			handleResult.put("errcode", ResponseErrorCode.SYS_ERROR.getCode());
			handleResult.put("errmsg", ResponseErrorCode.SYS_ERROR.getDesc());
		}
		
		return handleResult;
	}


	class ExecupteHelp implements Runnable{
		private JSONArray dataArray;
		private int startIndex;
		private int endIndex;
		private String actId;
		private Integer version;
		private CountDownLatch countDown;
		private Map<Integer, ModuleActRow> rowMap;
		public ExecupteHelp (JSONArray dataArray, int startIndex, int endIndex, String actId, Integer version, Map<Integer, ModuleActRow> rowMap, CountDownLatch countDown){
			this.dataArray  = dataArray ;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.actId = actId;
			this.version = version;
			this.rowMap = rowMap;
			this.countDown = countDown;
		}

		@Override
		public void run() {
			ActivityHandleDataUtils utils = new ActivityHandleDataUtils();
			if(dataArray != null && dataArray.size() > 0){
			    try {
                    for (int i = startIndex; i < endIndex; i++) {
                        JSONObject fieldObject = dataArray.getJSONObject(i);

                        String sysName = fieldObject.getString("sysName");
                        String modName = fieldObject.getString("modName");
                        String fieldType = fieldObject.getString("fieldType");
                        String fieldName = fieldObject.getString("fieldName");

                        fieldName = fieldName.toLowerCase();

                        String fieldLabel = fieldObject.getString("fieldLabel");
                        String fieldDesc = fieldObject.getString("fieldDesc");
                        Integer fieldIndex = fieldObject.getInteger("fieldIndex");

                        if(!sysName.equals(ActivityImportConstants.IMPORT_DEFAULT_SYSNAME)) {
                            String sysId = utils.saveSysDict(sysName, fieldIndex);

                            String modId = utils.saveModuleData(sysId, modName, fieldIndex);
                            String columnId = utils.saveModuleColumn(actId, modId, fieldName, fieldLabel, fieldDesc, fieldType, fieldIndex, version);

                            JSONArray rowDataArray = fieldObject.getJSONArray("dataValue");
                            for (int j = 0; j < rowDataArray.size(); j++) {
                                JSONObject rowValueObject = rowDataArray.getJSONObject(j);
                                Integer rowIndex = rowValueObject.getInteger("rowIndex");
                                String rowValue = rowValueObject.getString("rowValue");
                                utils.saveRowDataValue(fieldName, columnId, rowValue, version, rowMap.get(rowIndex), actId);
                            }
                        }
                    }
                } catch(Exception e){
			        e.printStackTrace();
                }
			}
            countDown.countDown();
			if(countDown.getCount() == 0){
				JedisUtils.set("thread_" + actId, "1", 300);
				if(rowMap != null && !rowMap.isEmpty()) {
					for(Integer rowIndex : rowMap.keySet()) {
						ModuleActRow row = rowMap.get(rowIndex);

						Integer count = moduleActValueDao.countByRowIdAndChange(row.getId(), row.getDelFlag());
						if(count > 0) {
							row.setChange(ActivityImportConstants.IMPORT_ROWDATA_VALUE_CHANGE);
							row.preUpdate();
							moduleActRowDao.update(row);
						}
					}
				}
			}
		}
	}
	
	public String saveSysDict(String sysName, Integer fieldIndex) {
		Dict parentDict = getParentDict();
		Dict dict = DictUtils.getByParentAndValue(parentDict, sysName);
		
		if(dict == null) {
			dict = new Dict(sysName, sysName);
			
			dict.preInsert();
			dict.setSort(fieldIndex);
			dict.setParent(parentDict);
			dict.setParentIds("0," + parentDict.getId());
			dictDao.insert(dict);
		}
		
		return dict.getId();
	}
	
	private Dict getParentDict() {
		Dict dict = DictUtils.getByTypeAndValue(ActivityImportConstants.ACT_SYS_TYPE, ActivityImportConstants.ACT_SYS_PARENT_NAME);
		if(dict == null) {
			dict = new Dict(ActivityImportConstants.ACT_SYS_PARENT_NAME, ActivityImportConstants.ACT_SYS_PARENT_LABEL);
			dict.preInsert();
			dict.setSort(60);
			dict.setType(ActivityImportConstants.ACT_SYS_TYPE);
			dictDao.insert(dict);
		}
		return dict;
	}
	
	private String saveModuleData(String sysId, String modName, Integer fieldIndex) {
		UcpModule tm = ucpModuleDao.getBySysAndName(sysId, modName);
		if(tm == null) {
			tm = new UcpModule();
			tm.preInsert();
			tm.setSysId(sysId);
			tm.setName(modName);
			tm.setSort(fieldIndex);
			ucpModuleDao.insert(tm);
		}
		return tm.getId();
	}
	
	private String saveModuleColumn(String actId, String modId, String fieldName, String fieldLabel, String fieldDesc, String fieldType, Integer fieldIndex, Integer version) {
		ModuleActColumn mac = new ModuleActColumn();
				//moduleActColumnDao.getByActIdAndNameAndVersion(actId, fieldName, version);
		boolean isNewRecord = true;
		/*if(mac == null) {
			isNewRecord = true;
			mac = new ModuleActColumn();
		}*/
		
		mac.setModuleId(modId);
		mac.setLabel(fieldLabel);
		mac.setDescription(fieldDesc);
		mac.setType(fieldType);
		mac.setSort(fieldIndex);
		
		if(isNewRecord) {
			mac.preInsert();
			mac.setActId(actId);
			mac.setName(fieldName.toLowerCase());
			mac.setVersion(version);
			moduleActColumnDao.insert(mac);
		} else {
			mac.preUpdate();
			mac.setDelFlag("0");
			moduleActColumnDao.update(mac);
		}
		return mac.getId();
	}
	
	private ModuleActRow saveMasterData(String rowValue, String actId, Integer version, Integer sort) {
		ModuleActRow mar = moduleActRowDao.getByActIdAndValueAndVersion(actId, rowValue, version);
		
		boolean isNewRecord = false;
		if(mar == null) {
			isNewRecord = true;
			mar = new ModuleActRow();
		}
		
		mar.setChange(ActivityImportConstants.IMPORT_ROWDATA_VALUE_NORMAL);
		mar.setSort(sort);
		if(isNewRecord) {
			mar.preInsert();
			mar.setActId(actId);
			mar.setConfigCertifiCenter(rowValue);
			mar.setVersion(version);
			
			mar.setConfigStatus(ModuleActRowStatus.PENDING.getAbbr());
			mar.setCheckStatus(ModuleActRowStatus.PENDING.getAbbr());
			
			if(version > 1) {
				ModuleActRow firstMar = moduleActRowDao.getByActIdAndValueAndVersion(actId, rowValue, 1);
				if(firstMar == null) {
					mar.setChange(ActivityImportConstants.IMPORT_ROWDATA_VALUE_CHANGE);
				}
			}
			
			moduleActRowDao.insert(mar);
		} else {
			mar.preUpdate();
			mar.setDelFlag("0");
			moduleActRowDao.update(mar);
		}
		return mar;
	}
	
	private void saveRowDataValue(String columnName, String columnId, String dataValue, Integer version, ModuleActRow row, String actId) {
		ModuleActValue mav = new ModuleActValue();

		boolean isNewRecord = true;
		mav.setValue(dataValue);
		mav.setChange(ActivityImportConstants.IMPORT_ROWDATA_VALUE_NORMAL);
		
		if(version > 1) {
			ModuleActValue firstMav = null;
			try {
				firstMav = moduleActValueDao.getFirstVersionValue(columnName, row.getConfigCertifiCenter(), actId);
			} catch(Exception e){
				logger.info("重复字段：" + columnName + "-----" + row.getConfigCertifiCenter() + "------" + actId);
			}
			if(firstMav != null) {
				if(StringUtils.isBlank(firstMav.getValue())) {
					mav.setChange(StringUtils.isNotBlank(dataValue) ? ActivityImportConstants.IMPORT_ROWDATA_VALUE_CHANGE : ActivityImportConstants.IMPORT_ROWDATA_VALUE_NORMAL);
				} else if(StringUtils.isNotBlank(firstMav.getValue())){
					mav.setChange(firstMav.getValue().equals(dataValue) ? ActivityImportConstants.IMPORT_ROWDATA_VALUE_NORMAL : ActivityImportConstants.IMPORT_ROWDATA_VALUE_CHANGE);
				}
			} else {
				mav.setChange(ActivityImportConstants.IMPORT_ROWDATA_VALUE_CHANGE);
			}
		}
		
		if(isNewRecord) {
			mav.preInsert();
			mav.setColumnId(columnId);
			mav.setRowId(row.getId());
			
			moduleActValueDao.insert(mav);
		} else {
			mav.preUpdate();
			mav.setDelFlag("0");
			moduleActValueDao.update(mav);
		}
	}
	
	private static void deletePrevVesionData(String actId, Integer version) {
		//删除module row value
		moduleActValueDao.deleteByActIdAndVersion(actId, version);

        //删除module column
        moduleActColumnDao.deleteByActIdAndVersion(actId, version);

        //删除module row
        moduleActRowDao.deleteByActIdAndVersion(actId, version);
	}
}
