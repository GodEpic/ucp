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
						sb.append("第" + (i + 1) + "行，第" + (j + 1) + "列，字段中文名为【" + fieldLabel + "】的数据不能为空，请检查之后重新导入").append("\n");
					}
					
					if(j == 1 && firstCellDataList != null) {
						if(firstCellDataList.contains(String.valueOf(cellData))) {
							sb.append("第" + (i + 1) + "行，第" + (j + 1) + "列，字段中文名为【" + fieldLabel + "】的数据【" + cellData + "】重复，请检查之后重新导入").append("\n");
						} else {
							firstCellDataList.add(String.valueOf(cellData));
						}
					}

					if (fieldType.equals(ImportType.DATE.getValue())) {
						try {
							Date date = dataCell.getDateCellValue();
							dataObject.put("rowValue", date != null ? DateUtils.formatDateTime(date) : null);
						} catch (Exception e) {
							sb.append("第" + (i + 1) + "行，第" + (j + 1) + "列，字段中文名为【" + fieldLabel + "】的数据格式不正确，请检查之后重新导入").append("\n");
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
							sb.append("第" + (i + 1) + "行，第" + (j + 1) + "列，字段中文名为【" + fieldLabel + "】的数据格式不正确，请检查之后重新导入").append("\n");
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
							sb.append("第" + (i + 1) + "行，第" + (j + 1) + "列，字段中文名为【" + fieldLabel + "】的数据格式不正确，请检查之后重新导入").append("\n");
						}
					} else if (fieldType.equals(ImportType.STRING.getValue())) {
						try {
							dataObject.put("rowValue", String.valueOf(cellData));
						} catch (Exception e) {
							sb.append("第" + (i + 1) + "行，第" + (j + 1) + "列，字段中文名为【" + fieldLabel + "】的数据格式不正确，请检查之后重新导入").append("\n");
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
					sysNameSB.append("第" + j + "列【券系统名】为空").append("\n");
				}
				
				if(StringUtils.isBlank(modName)) {
					modNameSB.append("第" + j + "列【券系统下的细分模块名】为空").append("\n");
				}
				
				if(StringUtils.isBlank(fieldName)) {
					fieldNameSB.append("第" + j + "列【字段英文名】为空").append("\n");
				}
				
				if(fieldList.contains(fieldName)) {
					fieldNameRepeatSB.append("第" + j + "列字段英文名【" + fieldName + "】重复").append("\n");
				} else {
					fieldList.add(fieldName);
				}
				
				if(StringUtils.isBlank(labelName)) {
					fieldLabelSB.append("第" + j + "列【字段中文名】为空").append("\n");
				}
				
				if(StringUtils.isBlank(fieldType)) {
					fieldTypeSB.append("第" + j + "列字段中文名为【" + labelName + "】的字段类型为空").append("\n");
				}
				
				if(StringUtils.isNotBlank(fieldType) && !ActivityImportConstants.existImportType(fieldType)) {
					fieldTypeExistSB.append("第" + j + "列字段中文名为【" + labelName + "】的字段类型不正确，请检查之后重新导入").append("\n");
				}
			}
		} catch (Exception e) {
			return "第" + currColIndex + "列解析错误：" + e.getMessage();
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
	 * 获取单元格值
	 *
	 * @param row    获取的行
	 * @param column 获取单元格列号
	 *
	 * @return 单元格值
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

	// 获取合并单元格集合
	public static List<CellRangeAddress> getCombineCellList(XSSFSheet sheet, int row, int firstColumn, int endColumn) {
		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
		// 获得一个 sheet 中合并单元格的数量
		int sheetMergerCount = sheet.getNumMergedRegions();
		// 遍历所有的合并单元格
		for (int i = 0; i < sheetMergerCount; i++) {
			// 获得合并单元格保存进list中
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

	// 获取合并单元格集合
	public static List<CellRangeAddress> getCombineCellList(XSSFSheet sheet, int row) {
		List<CellRangeAddress> list = new ArrayList<CellRangeAddress>();
		// 获得一个 sheet 中合并单元格的数量
		int sheetMergerCount = sheet.getNumMergedRegions();
		// 遍历所有的合并单元格
		for (int i = 0; i < sheetMergerCount; i++) {
			// 获得合并单元格保存进list中
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
		// 系统
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
