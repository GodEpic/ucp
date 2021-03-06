package com.yum.ucp.modules.task.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.constants.ActivityImportConstants;
import com.yum.ucp.common.constants.ActivityImportConstants.ImportType;
import com.yum.ucp.common.exception.ResponseErrorCode;
import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.common.utils.StringUtils;

public class ImportDataUtils {

	public static JSONObject importColumnDataExcel(InputStream is) {
		JSONObject result = new JSONObject();
		result.put("errcode", ResponseErrorCode.SUCCESS.getCode());
		result.put("errmsg", ResponseErrorCode.SUCCESS.getDesc());

		try {
			XSSFWorkbook wb = new XSSFWorkbook(is);
			XSSFSheet sheet = wb.getSheetAt(0);

			XSSFRow fieldRow = sheet.getRow(0);
			XSSFRow columnLabelRow = sheet.getRow(3);
			XSSFRow columnTypeRow = sheet.getRow(4);
			XSSFRow columnDescRow = sheet.getRow(5);
			
			String validResult = validExcel(sheet, fieldRow, columnLabelRow, columnTypeRow);
			
			if(StringUtils.isNotBlank(validResult)) {
				result.put("errcode", ResponseErrorCode.ERROR_IMPORT_VALID.getCode());
				result.put("errmsg", validResult);
				return result;
			}
			
			StringBuffer sb = new StringBuffer();

			JSONArray dataArray = new JSONArray();
			JSONObject firstObject = new JSONObject();
			int colLength = columnLabelRow.getPhysicalNumberOfCells();
			for (int j = 1; j < colLength; j++) {
				JSONObject columnObj = getCurrentSysName(sheet, j, new JSONObject());
				XSSFCell fieldCell = fieldRow.getCell(j);
				XSSFCell labelCell = columnLabelRow.getCell(j);
				XSSFCell typeCell = columnTypeRow.getCell(j);
				XSSFCell descCell = columnDescRow.getCell(j);

				String fieldName = fieldCell.getStringCellValue().toLowerCase();
				String fieldLabel = labelCell.getStringCellValue();

				if(StringUtils.isNotBlank(fieldName)){
					fieldName = fieldName.replaceAll("\n", "");
				}

				if(StringUtils.isNotBlank(fieldLabel)){
					fieldLabel = fieldLabel.replaceAll("\n", "");
				}

				String fieldDesc = descCell.getStringCellValue();
				String fieldType = typeCell.getStringCellValue();
				
				columnObj.put("fieldName", fieldName);
				columnObj.put("fieldLabel", fieldLabel);
				columnObj.put("fieldDesc", fieldDesc);
				columnObj.put("fieldType", fieldType);
				columnObj.put("fieldIndex", j);

				JSONArray dataValue = new JSONArray();
				
				List<String> firstCellDataList = null;
				if(j == 1) {
					firstCellDataList = new ArrayList<String>();
				}
				
				for (int i = 6; i <= sheet.getLastRowNum(); i++) {
					XSSFRow row = sheet.getRow(i);
					XSSFCell dataCell = row.getCell(j);

					JSONObject dataObject = new JSONObject();
					dataObject.put("rowIndex", i - 5);

					Object cellData = getCellValue(row, j);
					
					if(j == 1 && StringUtils.isBlank(String.valueOf(cellData))) {
						sb.append("???" + (i + 1) + "?????????" + (j + 1) + "???????????????????????????" + fieldLabel + "??????????????????????????????????????????????????????").append("\n");
					}
					
					if(j == 1 && firstCellDataList != null) {
						if(firstCellDataList.contains(String.valueOf(cellData))) {
							sb.append("???" + (i + 1) + "?????????" + (j + 1) + "???????????????????????????" + fieldLabel + "???????????????" + cellData + "???????????????????????????????????????").append("\n");
						} else {
							firstCellDataList.add(String.valueOf(cellData));
						}
					}

					if (fieldType.equals(ImportType.DATE.getValue())) {
						try {
							Date date = dataCell.getDateCellValue();
							dataObject.put("rowValue", date != null ? DateUtils.formatDateTime(date) : null);
						} catch (Exception e) {
							sb.append("???" + (i + 1) + "?????????" + (j + 1) + "???????????????????????????" + fieldLabel + "?????????????????????????????????????????????????????????").append("\n");
						}
					} else if (fieldType.equals(ImportType.INTEGER.getValue())) {
						try {
							String data = String.valueOf(cellData);
							if(StringUtils.isNotBlank(data)) {
								dataObject.put("rowValue", Integer.valueOf(data));
							} else {
								dataObject.put("rowValue", null);
							}
						} catch (Exception e) {
							sb.append("???" + (i + 1) + "?????????" + (j + 1) + "???????????????????????????" + fieldLabel + "?????????????????????????????????????????????????????????").append("\n");
						}
					} else if (fieldType.equals(ImportType.DOUBLE.getValue())) {
						try {
							String data = String.valueOf(cellData);
							if(StringUtils.isNotBlank(data)) {
								dataObject.put("rowValue", Double.valueOf(data));
							} else {
								dataObject.put("rowValue", null);
							}
						} catch (Exception e) {
							sb.append("???" + (i + 1) + "?????????" + (j + 1) + "???????????????????????????" + fieldLabel + "?????????????????????????????????????????????????????????").append("\n");
						}
					} else if (fieldType.equals(ImportType.STRING.getValue())) {
						try {
							dataObject.put("rowValue", String.valueOf(cellData));
						} catch (Exception e) {
							sb.append("???" + (i + 1) + "?????????" + (j + 1) + "???????????????????????????" + fieldLabel + "?????????????????????????????????????????????????????????").append("\n");
						}
					}
					
					dataValue.add(dataObject);
				}
				columnObj.put("dataValue", dataValue);
				if(j == 1){
					firstObject = columnObj;
				}
				dataArray.add(columnObj);
			}
			String resultMsg = sb.toString();
			if(StringUtils.isNotBlank(resultMsg)) {
				if(resultMsg.endsWith("\n")) {
					resultMsg = resultMsg.substring(0, resultMsg.length() - 1);
				}
				
				result.put("errcode", ResponseErrorCode.ERROR_IMPORT_VALID.getCode());
				result.put("errmsg", resultMsg);
				return result;
			}
			result.put("data", dataArray);
			result.put("rowData", firstObject);
			result.put("dataSize", sheet.getLastRowNum() - 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String validExcel(XSSFSheet sheet, XSSFRow fieldRow, XSSFRow columnLabelRow, XSSFRow columnTypeRow) {
		
		StringBuffer sysNameSB = new StringBuffer();
		StringBuffer modNameSB = new StringBuffer();
		
		StringBuffer fieldNameSB = new StringBuffer();
		StringBuffer fieldLabelSB = new StringBuffer();
		StringBuffer fieldTypeSB = new StringBuffer();
		StringBuffer fieldTypeExistSB = new StringBuffer();
		StringBuffer fieldNameRepeatSB = new StringBuffer();
		int currColIndex = 1;
		try {
			int colLength = fieldRow.getPhysicalNumberOfCells();
			List<String> fieldList = new ArrayList<String>();
			for (int j = 1; j < colLength; j++) {
				XSSFCell fieldCell = fieldRow.getCell(j);
				XSSFCell labelCell = columnLabelRow.getCell(j);
				XSSFCell typeCell = columnTypeRow.getCell(j);

				currColIndex = j;

				String sysName = getSysOrModName(sheet, j, 1);
				String modName = getSysOrModName(sheet, j, 2);

				String fieldName = fieldCell.getStringCellValue().toLowerCase();
				String labelName = labelCell.getStringCellValue();

				if(StringUtils.isNotBlank(fieldName)){
					fieldName = fieldName.replaceAll("\n", "");
				}

				if(StringUtils.isNotBlank(labelName)){
					labelName = labelName.replaceAll("\n", "");
				}

				String fieldType = typeCell.getStringCellValue();
				
				if(StringUtils.isBlank(sysName)) {
					sysNameSB.append("???" + j + "???????????????????????????").append("\n");
				}
				
				if(StringUtils.isBlank(modName)) {
					modNameSB.append("???" + j + "?????????????????????????????????????????????").append("\n");
				}
				
				if(StringUtils.isBlank(fieldName)) {
					fieldNameSB.append("???" + j + "??????????????????????????????").append("\n");
				}
				
				if(fieldList.contains(fieldName)) {
					fieldNameRepeatSB.append("???" + j + "?????????????????????" + fieldName + "?????????").append("\n");
				} else {
					fieldList.add(fieldName);
				}
				
				if(StringUtils.isBlank(labelName)) {
					fieldLabelSB.append("???" + j + "??????????????????????????????").append("\n");
				}
				
				if(StringUtils.isBlank(fieldType)) {
					fieldTypeSB.append("???" + j + "????????????????????????" + labelName + "????????????????????????").append("\n");
				}
				
				if(StringUtils.isNotBlank(fieldType) && !ActivityImportConstants.existImportType(fieldType)) {
					fieldTypeExistSB.append("???" + j + "????????????????????????" + labelName + "?????????????????????????????????????????????????????????").append("\n");
				}
			}
		} catch (Exception e) {
			return "???" + currColIndex + "??????????????????" + e.getMessage();
		}
		
		if(StringUtils.isNotBlank(modNameSB.toString())) {
			modNameSB.append("\n");
		}
		
		if(StringUtils.isNotBlank(fieldNameSB.toString())) {
			fieldNameSB.append("\n");
		}
		
		if(StringUtils.isNotBlank(fieldNameRepeatSB.toString())) {
			fieldNameRepeatSB.append("\n");
		}
		
		if(StringUtils.isNotBlank(fieldLabelSB.toString())) {
			fieldLabelSB.append("\n");
		}
		
		if(StringUtils.isNotBlank(fieldTypeSB.toString())) {
			fieldTypeSB.append("\n");
		}
		
		sysNameSB.append(modNameSB).append(fieldNameSB).append(fieldNameRepeatSB).append(fieldLabelSB).append(fieldTypeSB.toString()).append(fieldTypeExistSB);
		
		String result = sysNameSB.toString();
		if(result.endsWith("\n")) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * ??????????????????
	 *
	 * @param row    ????????????
	 * @param column ?????????????????????
	 *
	 * @return ????????????
	 */
	public static Object getCellValue(XSSFRow row, int column) {
		Object val = "";
		try {
			XSSFCell cell = row.getCell(column);
			if (cell == null) {
				return val;
			}
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_NUMERIC:
				DecimalFormat df = new DecimalFormat("0");
				val = df.format(cell.getNumericCellValue());
				break;
			case XSSFCell.CELL_TYPE_STRING:
				val = cell.getStringCellValue();
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				val = cell.getCellFormula();
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN:
				val = cell.getBooleanCellValue();
				break;
			case XSSFCell.CELL_TYPE_ERROR:
				val = cell.getErrorCellValue();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	// ???????????????????????????
	public static List<CellRangeAddress> getCombineCellList(XSSFSheet sheet, int row, int firstColumn, int endColumn) {
		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
		// ???????????? sheet ???????????????????????????
		int sheetMergerCount = sheet.getNumMergedRegions();
		// ??????????????????????????????
		for (int i = 0; i < sheetMergerCount; i++) {
			// ??????????????????????????????list???
			CellRangeAddress ca = sheet.getMergedRegion(i);

			if (firstColumn == -1 || endColumn == -1) {
				if (ca.getFirstRow() == row) {
					list.add(ca);
				}
			} else {
				if (ca.getFirstRow() == row && ca.getFirstColumn() >= firstColumn && ca.getLastColumn() <= endColumn) {
					list.add(ca);
				}
			}
		}
		return list;
	}

	// ???????????????????????????
	public static List<CellRangeAddress> getCombineCellList(XSSFSheet sheet, int row) {
		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
		// ???????????? sheet ???????????????????????????
		int sheetMergerCount = sheet.getNumMergedRegions();
		// ??????????????????????????????
		for (int i = 0; i < sheetMergerCount; i++) {
			// ??????????????????????????????list???
			CellRangeAddress ca = sheet.getMergedRegion(i);
			if (ca.getFirstRow() == row) {
				list.add(ca);
			}
		}
		return list;
	}

	public static JSONObject getCurrentSysName(XSSFSheet sheet, int columnIndex, JSONObject cellObj) {
		cellObj.put("sysName", getSysOrModName(sheet, columnIndex, 1));
		cellObj.put("modName", getSysOrModName(sheet, columnIndex, 2));
		return cellObj;
	}
	
	public static String getSysOrModName(XSSFSheet sheet, int columnIndex, int rowIndex) {
		String resultName = "";
		
		List<CellRangeAddress> cellList = getCombineCellList(sheet, rowIndex);
		XSSFRow row = sheet.getRow(rowIndex);

		if (columnIndex == 1) {
			return ActivityImportConstants.IMPORT_DEFAULT_SYSNAME;
		}
		// ??????
		for (CellRangeAddress crd : cellList) {
			if (columnIndex >= crd.getFirstColumn() && columnIndex <= crd.getLastColumn()) {
				XSSFCell cell = row.getCell(crd.getFirstColumn());
				if(StringUtils.isNotBlank(cell.getStringCellValue())) {
					resultName = cell.getStringCellValue();
					break;
				} else {
					cell = row.getCell(columnIndex);
					resultName = cell.getStringCellValue();
					break;
				}
			}
		}
		
		if(StringUtils.isBlank(resultName)) {
			XSSFCell cell = row.getCell(columnIndex);
			resultName = cell.getStringCellValue();
		}
		
		return resultName;
	}

	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream("E:\\20190718.xlsx");
			JSONObject result = importColumnDataExcel(is);
			JSONArray dataArray = result.getJSONArray("data");
			System.out.println((Integer)(dataArray.size()/10));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
