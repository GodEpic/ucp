package com.yum.ucp.modules.activity.web;

import com.atlassian.jira.rest.client.api.RestClientException;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.utils.DateUtils;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.activity.http.NotifyInterfaceService;
import com.yum.ucp.modules.activity.utils.AttachUtils;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.utils.DictUtils;
import com.yum.ucp.modules.sys.utils.MessageUtils;
import com.yum.ucp.modules.sys.utils.UserUtils;
import com.yum.ucp.modules.task.entity.ModuleActRow;
import com.yum.ucp.modules.task.entity.ModuleActValue;
import com.yum.ucp.modules.task.service.ModuleActRowService;
import com.yum.ucp.modules.task.service.ModuleActValueService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.modules.activity.entity.QaCheck;
import com.yum.ucp.modules.activity.entity.QaReport;
import com.yum.ucp.modules.activity.entity.QaResult;
import com.yum.ucp.modules.activity.entity.UcpActFile;
import com.yum.ucp.modules.activity.service.ActivityService;
import com.yum.ucp.modules.activity.service.QaCheckService;
import com.yum.ucp.modules.activity.service.QaReportService;
import com.yum.ucp.modules.activity.service.QaResultService;
import com.yum.ucp.modules.activity.service.UcpActFileService;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.sys.entity.Attach;
import com.yum.ucp.modules.sys.service.AttachService;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import com.yum.ucp.modules.task.utils.ModuleActRowUtils;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * QA????????????Controller
 * @author Zachary
 * @version 2019-08-14
 */
@Controller
@RequestMapping(value = "${adminPath}/activity/qa")
public class QaController extends DscBaseController{
	@Autowired
    private ActivityService activityService;
	
	@Autowired
	private QaReportService qaReportService;
	
	@Autowired
	private AttachService attachService;
	
	@Autowired
	private QaCheckService qaCheckService;
	
	@Autowired
	private QaResultService qaResultService;

	@Autowired
    private ModuleActRowService moduleActRowService;

	@Autowired
	private ModuleActValueService moduleActValueService;

	@Autowired
	NotifyInterfaceService notifyInterfaceService;
	
	@Autowired
    private UcpActFileService actFileService;

	/**
	 * ???????????????????????????
	 * @param actId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/detail")
    public String detail(String actId, Model model) throws Exception {
        model.addAttribute("activity", activityService.get(actId));
		//?????????
		activityStatus(model);
        model.addAttribute("rowArray", ModuleActRowUtils.findModuleRowArrayByActId(moduleActRowService.findModuleRowQaResult(actId, 0)));
        
        JSONArray actFileData = new JSONArray();
        if (!StringUtils.isEmpty(actId)) {
            List<UcpActFile> lists = actFileService.findByActId(actId);
            if (!CollectionUtils.isEmpty(lists)) {
                lists.forEach(actFile -> {
                    JSONObject content = JSONObject.parseObject(actFile.getContent());
                    actFileData.add(content);
                });
            }
        }
        model.addAttribute("actFileData", actFileData);
        model.addAttribute("filePath", Global.getConfig(Global.FTP_PATH_URL));
        
        return "ucp/qa/detail";
    }

	/**
	 * ?????????QA??????????????????
	 * @param actId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/approval")
    public String approval(String actId, Model model) throws Exception {
		Activity activity = activityService.get(actId);
		QaReport qrt = qaReportService.getByActId(actId);

		model.addAttribute("activity", activity);
		model.addAttribute("qaReport", qrt);
        model.addAttribute("className", QaReport.class.getName());
		if(qrt != null){
            model.addAttribute("attachList", AttachUtils.findListByClassAndType(QaReport.class.getName(), qrt.getId(), ""));
        }

		if(qrt != null && activity.getStatus().equals(ACTIVITY_STATUS_TEST_PASS) || !activity.getReceiveUser().equals(UserUtils.getCache("phoneAgent"))){
			return "redirect:" + adminPath + "/activity/qa/report?actId=" + actId;
		}

		activityStatus(model);
        return "ucp/qa/approval";
    }

	/**
	 * ?????????QA?????????????????????
	 * @param actId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/reject")
    public String reject(String actId, Model model) throws Exception {
		Activity activity = activityService.get(actId);
		/*Activity activity = activityService.get(actId);
		if(activity.getStatus().equals(ACTIVITY_STATUS_TEST_NOT_PASS)){
			return "redirect:" + adminPath + "/activity/qa/feedBackReportDetail?actId=" + actId;
		}

		if(activity.getStatus().equals(ACTIVITY_STATUS_TEST_FEEDBACK)){
			return "redirect:" + adminPath + "/activity/qa/recheck?actId=" + actId;
		}*/

		activityStatus(model);
		if(StringUtils.isNotBlank(activity.getStatus()) && activity.getStatus().equals(ACTIVITY_STATUS_TESTING)){
			model.addAttribute("actId", actId);
			model.addAttribute("activity", activity);
			model.addAttribute("rowArray", ModuleActRowUtils.findModuleRowArrayByActId(moduleActRowService.findModuleRowQaResult(actId, 0)));
			model.addAttribute("checkType", DictUtils.formatDictList("qa_check_type"));
			model.addAttribute("pageType", "check");

			return "ucp/qa/reject";
		}
		return "redirect:" + adminPath + "/activity/qa/detail?actId=" + actId;
    }

	/**
	 * ?????????QA?????????????????????
	 * @param actId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/recheck")
	public String recheck(String actId, Model model) throws Exception {


		QaResult lastResult = qaResultService.findLastVersionByActId(actId);

		List<ModuleActRow> rowLists = moduleActRowService.findModuleRowQaResult(actId, lastResult.getVersion());

		activityStatus(model);
		model.addAttribute("actId", actId);
		model.addAttribute("activity", activityService.get(actId));
		model.addAttribute("rowArray", ModuleActRowUtils.findModuleRowArrayByActId(rowLists));
		model.addAttribute("checkType", DictUtils.formatDictList("qa_check_type"));

		model.addAttribute("moduleRowResult", rowLists);
		model.addAttribute("qaCheckList", qaCheckService.findListByActIdAndVersion(actId));
		model.addAttribute("qaCheckClassName", QaCheck.class.getName());
        model.addAttribute("pageType", "recheck");
		model.addAttribute("reportAttach", attachService.getByClassNameAndClassPk(new Attach(QaResult.class.getName(), lastResult.getId(), "")));
		return "ucp/qa/reject";
	}

    /**
     * ??????QA?????????????????????
     * @param report
     * @param eatinImage
     * @param mobileImage
     * @param outwardImage
     * @param takeoutImage
     * @param kioskImage
     * @param silverSecondImage
     * @param posImage
     * @param invoiceImage
     * @param otherImage
     * @return
     * @throws Exception
     */
    @RequestMapping("saveDraftApproval")
    @ResponseBody
    public ResponseMessage saveDraftApproval(QaReport report, String eatinImage, String mobileImage, String outwardImage, String takeoutImage, String kioskImage, String silverSecondImage, String posImage, String invoiceImage, String otherImage) throws Exception {
        try{
            saveTestReport(report, eatinImage, mobileImage, outwardImage, takeoutImage, kioskImage, silverSecondImage, posImage, invoiceImage, otherImage,true);
        } catch (Exception e){
            return ResponseMessage.error("?????????????????????????????????" + e.getMessage());
        }

        return ResponseMessage.success();
    }

	/**
	 * ??????QA?????????????????????
	 * @param report
	 * @param eatinImage
	 * @param mobileImage
	 * @param outwardImage
	 * @param takeoutImage
	 * @param kioskImage
	 * @param silverSecondImage
	 * @param posImage
	 * @param invoiceImage
	 * @param otherImage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveApproval")
    @ResponseBody
    public ResponseMessage saveApproval(QaReport report, String eatinImage, String mobileImage, String outwardImage, 
    		String takeoutImage, String kioskImage, String silverSecondImage, String posImage, String invoiceImage, String otherImage) throws Exception {

        try{
            saveTestReport(report, eatinImage, mobileImage, outwardImage, takeoutImage, kioskImage, silverSecondImage, posImage, invoiceImage, otherImage, false);
        } catch (Exception e){
            return ResponseMessage.error("?????????????????????????????????" + e.getMessage());
        }

        try{
            updateActivityStatus(report.getActId(), ACTIVITY_JIRA_STATUS_DONE, ACTIVITY_STATUS_TEST_PASS, QA_TO_DONE);
        } catch (Exception e){
			logger.error("??????JIRA???????????????", e);
            return ResponseMessage.error("??????JIRA???????????????" + e.getMessage());
        }

        return ResponseMessage.success();
    }

    private void saveTestReport(QaReport report, String eatinImage, String mobileImage, String outwardImage,
                           String takeoutImage, String kioskImage, String silverSecondImage, String posImage, String invoiceImage, String otherImage, boolean isDraft) throws Exception{

	    qaReportService.save(report);

        String reportId = report.getId();

		saveReportAttach("eatinImage", eatinImage, QaReport.class.getName(), reportId);
		saveReportAttach("mobileImage", mobileImage, QaReport.class.getName(), reportId);
		saveReportAttach("outwardImage", outwardImage, QaReport.class.getName(), reportId);
		saveReportAttach("takeoutImage", takeoutImage, QaReport.class.getName(), reportId);
		saveReportAttach("kioskImage", kioskImage, QaReport.class.getName(), reportId);
		saveReportAttach("silverSecondImage", silverSecondImage, QaReport.class.getName(), reportId);
		saveReportAttach("posImage", posImage, QaReport.class.getName(), reportId);
		saveReportAttach("invoiceImage", invoiceImage, QaReport.class.getName(), reportId);
		saveReportAttach("otherImage", otherImage, QaReport.class.getName(), reportId);

		if(!isDraft) {
			saveQaResult(report.getActId(), "0");
		}
    }

	/**
	 * ???????????????????????????????????????????????????
	 * @param type
	 * @param imageArray
	 * @param className
	 * @param classPk
	 */
	private void saveReportAttach(String type, String imageArray, String className, String classPk){
		attachService.deleteByClassNameAndClassPkAndType(new Attach(className, classPk, type));
		saveQaAttach(type, imageArray, className, classPk);
	}

	/**
	 * ??????QA????????????????????????
	 * @param feedBackData
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("saveQaCheck")
    @ResponseBody
    public ResponseMessage saveQaCheck(String feedBackData, String actId, String checkReportFile) throws Exception {
		feedBackData = HtmlUtils.htmlUnescape(feedBackData);

		try {
			JSONArray feedBackArray = JSONArray.parseArray(feedBackData);
			Integer checkVersion = qaCheckService.getCurrentVersionByActId(actId);
			for(int i = 0; i < feedBackArray.size(); i++) {
				JSONObject item = feedBackArray.getJSONObject(i);

				QaCheck qaCheck = new QaCheck();
				qaCheck.setActId(actId);
				qaCheck.setCouponId(item.getString("cpRowId"));
				qaCheck.setDescription(item.getString("remark"));
				qaCheck.setType(item.getString("type"));
				qaCheck.setVersion(checkVersion);

				qaCheckService.save(qaCheck);

				JSONArray itemImgArray = item.getJSONArray("imgSrc");
				saveQaAttach("feedBack", itemImgArray.toString(), QaCheck.class.getName(), qaCheck.getId());
			}
			String qaResultId = saveQaResult(actId, "1");

			if(StringUtils.isNotBlank(checkReportFile)){
				JSONArray reportArray = JSONArray.parseArray(HtmlUtils.htmlUnescape(checkReportFile));
				saveQaAttach("checkReport", reportArray.toString(), QaResult.class.getName(), qaResultId);
			}
		} catch(Exception e){
			logger.error("??????????????????????????????", e);
			return ResponseMessage.error("??????????????????????????????" + e.getMessage());
		}

		try {
			updateActivityStatus(actId, ACTIVITY_JIRA_STATUS_SENIOR, ACTIVITY_STATUS_TEST_NOT_PASS, QA_TO_SENIOR);
		} catch(RestClientException e){
			logger.error("??????JIRA???????????????", e);
			return ResponseMessage.error(MessageUtils.returnErrors(e.getErrorCollections(), findAllFields()));
		}

		return ResponseMessage.success();
	}

	/**
	 * ??????????????????
	 * @param type
	 * @param imageArray
	 * @param className
	 * @param classPk
	 */
	private void saveQaAttach(String type, String imageArray, String className, String classPk) {
		JSONArray items = JSONArray.parseArray(HtmlUtils.htmlUnescape(imageArray));
		if(items != null && !items.isEmpty()) {
			
			for(int i = 0; i < items.size(); i++) {
				JSONObject item = items.getJSONObject(i);
				Attach attach = new Attach();
				
				attach.setClassName(className);
				attach.setClassPk(classPk);
				attach.setOriginalFileName(item.getString("originalFileName"));
				attach.setFileSize(item.getString("fileSize"));
				attach.setExtension(item.getString("ext"));
				attach.setFileName(item.getString("fileName"));
				attach.setFilePath(item.getString("filePath"));
				attach.setType(type);
				
				attachService.save(attach);
			}
		}
	}

    /**
     * ??????QA????????????
     * @param actId
     * @param result
     * @return
     */
	private String saveQaResult(String actId, String result) {
		Integer version = qaResultService.getCurrentVersionByActId(actId);
		
		QaResult qrt = new QaResult();
		
		qrt.setActId(actId);
		qrt.setResult(result);
		qrt.setVersion(version);
		
		qaResultService.save(qrt);

		return qrt.getId();
	}

    /**
     * ??????????????????????????????
     * @param actId
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping("/report")
    public String qaTestReport(String actId, Model model) throws Exception {
		QaReport qrt = qaReportService.getByActId(actId);
		if(qrt != null) {
			model.addAttribute("qaReport", qaReportService.getByActId(actId));
	        model.addAttribute("attachList", AttachUtils.findListByClassAndType(QaReport.class.getName(), qrt.getId(), ""));
	        model.addAttribute("className", QaReport.class.getName());
		}
        return "ucp/qa/qaTestReport";
    }

    /**
     * ??????????????????????????????
     * @param actId
     * @param model
     * @return
     * @throws Exception
     */
	@RequestMapping("/feedBack")
    public String qaFeedBackReport(String actId, Model model) throws Exception {

		QaResult lastResult = qaResultService.findLastVersionByActId(actId);

		List<ModuleActRow> rowLists = moduleActRowService.findModuleRowQaResult(actId, lastResult.getVersion());
		activityStatus(model);
        model.addAttribute("actId", actId);
		model.addAttribute("activity", activityService.get(actId));
        model.addAttribute("moduleRowResult", rowLists);
        model.addAttribute("qaCheckList", qaCheckService.findListByActIdAndVersion(actId));
        model.addAttribute("qaCheckClassName", QaCheck.class.getName());
		model.addAttribute("reportAttach", attachService.getByClassNameAndClassPk(new Attach(QaResult.class.getName(), lastResult.getId(), "")));
        return "ucp/qa/qaFeedBackReport";
    }

	/**
	 * ????????????????????????????????????
	 * @param actId
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/feedBackReportDetail")
	public String feedBackReportDetail(String actId, Model model) {
		QaResult lastResult = qaResultService.findLastVersionByActId(actId);

		if(lastResult != null){
			List<ModuleActRow> rowLists = moduleActRowService.findModuleRowQaResult(actId, lastResult.getVersion());

			model.addAttribute("actId", actId);
			model.addAttribute("moduleRowResult", rowLists);
			model.addAttribute("qaCheckList", qaCheckService.findListByActIdAndVersion(actId));
			model.addAttribute("qaCheckClassName", QaCheck.class.getName());
			model.addAttribute("reportAttach", attachService.getByClassNameAndClassPk(new Attach(QaResult.class.getName(), lastResult.getId(), "")));
		}
		return "ucp/qa/feedBackReportDetail";
	}

    /**
     * ????????????????????????????????????
     * @param qaCheckId
     * @return
     * @throws Exception
     */
    @RequestMapping("saveIgnoreCheck")
    @ResponseBody
    public ResponseMessage saveIgnoreCheck(String qaCheckId) throws Exception {
		QaCheck check = qaCheckService.get(qaCheckId);
		if(check != null){
			check.setIgnore("0");
			check.setFeedback("");

			qaCheckService.save(check);
		}
        return ResponseMessage.success();
    }

    /**
     * ??????????????????????????????
     * @param qaCheckId
     * @return
     * @throws Exception
     */
    @RequestMapping("saveFeedBack")
    @ResponseBody
    public ResponseMessage saveFeedBack(String qaCheckId, String feedBack) throws Exception {
		QaCheck check = qaCheckService.get(qaCheckId);
		if(check != null){
			check.setIgnore("1");
			check.setFeedback(feedBack);
			qaCheckService.save(check);
		}
        return ResponseMessage.success();
    }

	/**
	 * ???????????????????????????QA
	 * @param actId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("submitToQa")
	@ResponseBody
	public ResponseMessage submitToQa(String actId) throws Exception {
		Activity activity = activityService.get(actId);
		if(activity != null){
			if(qaCheckService.findAllQaCheckIsFeedBack(actId) > 0){
				return ResponseMessage.error("???????????????????????????????????????????????????????????????QA");
			}
			updateActivityStatus(actId, ACTIVITY_JIRA_STATUS_QA, ACTIVITY_STATUS_TEST_FEEDBACK, SENIOR_TO_QA);
		}
		return ResponseMessage.success();
	}

	private void updateActivityStatus(String actId, String jiraStatus, String status, String issusStatus){
		Activity activity = activityService.get(actId);
		activity.setJiraStatus(jiraStatus);
		activity.setStatus(status);

		activityService.save(activity);

		if(status.equals(ACTIVITY_STATUS_TEST_PASS)){
			notifyInterfaceService.notivifySuccess(activity);
		}
		updateIssueStatus(activity.getJiraNo(), issusStatus);
	}


	@RequestMapping("test")
	@ResponseBody
	public void test(String actId) throws Exception {
		notifyInterfaceService.notivifySuccess(activityService.get(actId));
	}


		/**
         * ??????????????????
         * @param actId
         * @return
         * @throws Exception
         */
	@RequestMapping("exportTestTask")
	public void exportTestTask(String actId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			Activity activity = activityService.get(actId);

            String fileName = "??????????????????_" + DateUtils.formatDate(new Date(), "yyyyMMddHHmmss") + ".xls";

            OutputStream out = response.getOutputStream();
            // ???????????????(excel)
            Workbook wb = new HSSFWorkbook();
            /* ??????sheet */
            Sheet sheet = wb.createSheet("??????????????????");

            // ??????????????????
            Font fontTitle = wb.createFont();
            fontTitle.setFontHeightInPoints((short) 12); // ????????????
            fontTitle.setColor(HSSFColor.BLACK.index); // ????????????
            fontTitle.setBoldweight(Font.BOLDWEIGHT_BOLD); // ????????????

            // ???????????????????????????
            CellStyle cellStyleTitle = wb.createCellStyle();
            cellStyleTitle.setFont(fontTitle);
            cellStyleTitle.setAlignment(CellStyle.ALIGN_CENTER); // ?????????????????????
            cellStyleTitle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cellStyleTitle.setWrapText(true);// ??????????????????

			// ???????????????????????????
			CellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // ?????????????????????
			cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			cellStyle.setWrapText(true);// ??????????????????

            int index = 0;
            Row firstRow = createRow(index++, sheet);

            createHeaderCell(sheet, firstRow, 0, 0, 0, 17, "??????Team??????", cellStyleTitle);
            createHeaderCell(sheet, firstRow, 0, 0, 18, 18, "????????????Team", cellStyleTitle);
            createHeaderCell(sheet, firstRow, 0, 0, 19, 36, "??????QA Team", cellStyleTitle);

            Row secondRow = createRow(index++, sheet);
            String[] shTeamHeader = { "?????????", "???ID", "??????", "??????/??????/V??? ??????", "??????????????????????????????????????????", "???????????????", "??????????????????-??????/??????", "??????????????????", "????????????", "????????????????????????", "??????????????????/??????/??????", "????????????Overing", "??????????????????", "??????????????????", "?????????????????????", "Prime????????????", "Prime??????", "??????"};
            int colIndex = 0;
            for(String cellValue : shTeamHeader){
                createHeaderCell(sheet, secondRow, 1, 2, colIndex, colIndex++, cellValue, cellStyleTitle);
            }

            String[] xaConfigTeamHeader = {"????????????"};
            for(String cellValue : xaConfigTeamHeader){
                createHeaderCell(sheet, secondRow, 1, 2, colIndex, colIndex++, cellValue, cellStyleTitle);
            }

            String[] xaQaTeamHeader = { "????????????????????????", "??????/??????/??????????????????"};
            for(String cellValue : xaQaTeamHeader){
                createHeaderCell(sheet, secondRow, 1, 2, colIndex, colIndex++, cellValue, cellStyleTitle);
            }

            String[] header = { "??????", "??????", "????????????", "Kiosk", "??????"};
            int startColIndex = colIndex;
            for(String cellValue : header){
                createHeaderCell(sheet, secondRow, 1, 1, colIndex, colIndex + 1, cellValue, cellStyleTitle);
                colIndex += 2;
            }

            Row thirdrow = createRow(index++, sheet);
            colIndex = startColIndex;
            String[] thirdHeader = {"UAT", "??????", "UAT", "??????", "UAT", "??????", "UAT", "??????", "UAT", "??????"};
            for(String cellValue : thirdHeader){
                createHeaderCell(sheet, thirdrow, 2, 2, colIndex, colIndex++, cellValue, cellStyleTitle);
            }

            String[] lastHeader = {"??????", "?????????", "????????????", "????????????", "Prime????????????", "Prime??????"};
            for(String cellValue : lastHeader){
                createHeaderCell(sheet, secondRow, 1, 2, colIndex, colIndex++, cellValue, cellStyleTitle);
            }

			if(activity != null){
            	String actName = activity.getActivityName();
            	long startTime = new Date().getTime();
				System.out.println("startTime : " + startTime);
				List<ModuleActRow> rowLists = moduleActRowService.findModuleRowQaResult(actId, 0);
				long endTime = new Date().getTime();
				System.out.println("endTime : " + endTime);
				System.out.println("execTime : " + (endTime - startTime));
				if(rowLists != null && !rowLists.isEmpty()){
					int rowColIndex = 0;
					Integer version = moduleActRowService.getModuleRowLastVersionByActId(actId);
					for (int i = 0; i < rowLists.size(); i++){
						ModuleActRow row = rowLists.get(i);
						thirdrow = createRow(index++, sheet);
						//?????????
						createRowCell(thirdrow, rowColIndex++, actName, cellStyle);
						List<ModuleActValue> lists = moduleActValueService.exportRowDataByActIdAndRowId(actId, row.getId(), version);

						if(lists != null && !lists.isEmpty()){
							for(int j = 0; j < lists.size(); j++){
								ModuleActValue mac = lists.get(j);

								createRowCell(thirdrow, rowColIndex++, mac.getValue(), cellStyle);
								if(j == lists.size() - 1){
									rowColIndex = 0;
								}
							}
						}
					}
				}


			}

            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");

            wb.write(out);

            out.flush();
            out.close();
		} catch (Exception e){
            e.printStackTrace();
		}
	}


	public static Cell createRowCell(Row row, int colIndex, String cellValue, CellStyle cellStyle){
		Cell cell = row.createCell(colIndex);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);// ????????????
		return cell;
	}

	public static Cell createHeaderCell(Sheet sheet, Row row, int firstRowIndex, int lastRowIndex, int firstColIndex, int lastColIndex, String cellValue, CellStyle cellStyle){
		if(firstRowIndex != lastRowIndex || firstColIndex != lastColIndex){
			sheet.addMergedRegion(new CellRangeAddress(firstRowIndex, lastRowIndex, firstColIndex, lastColIndex));
		}
		Cell cell = row.createCell(firstColIndex);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);// ????????????
		if(firstColIndex == lastColIndex){
			sheet.setColumnWidth(firstColIndex, 30 * 256);
		}
		return cell;
	}

	private static Row createRow(int index, Sheet sheet){
		Row row = sheet.createRow(index);
		row.setHeightInPoints(30);
		return row;
	}

}
