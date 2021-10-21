package com.yum.ucp.modules.Interface.web;

import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.persistence.BaseEntity;
import com.yum.ucp.common.utils.FtpUtils;
import com.yum.ucp.common.utils.IdGen;
import com.yum.ucp.common.utils.SnowFlakeUtils;
import com.yum.ucp.modules.Interface.entity.*;
import com.yum.ucp.modules.activity.dao.NotifyFileDao;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.activity.entity.NotifyActivity;
import com.yum.ucp.modules.activity.entity.NotifyFile;
import com.yum.ucp.modules.activity.entity.NotifyRefActivity;
import com.yum.ucp.modules.activity.service.ActivityService;
import com.yum.ucp.modules.activity.service.NotifyActivityService;
import com.yum.ucp.modules.activity.service.NotifyFileService;
import com.yum.ucp.modules.activity.service.NotifyRefActiviryService;
import com.yum.ucp.modules.common.web.DscBaseController;
import com.yum.ucp.modules.enumerate.NotifyActivityStatus;
import com.yum.ucp.modules.exception.NotifyException;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import com.yum.ucp.modules.task.entity.UcpTask;
import com.yum.ucp.modules.task.entity.UcpTaskReceive;
import com.yum.ucp.modules.task.service.UcpTaskReceiveService;
import com.yum.ucp.modules.task.service.UcpTaskService;
import kong.unirest.ContentType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/activity/notify")
public class ActivityNotifyController {

	Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private NotifyActivityService notifyActivityService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private UcpTaskService ucpTaskService;

	@Autowired
	private UcpTaskReceiveService ucpTaskReceiveService;

	@Autowired
	private NotifyFileService notifyFileService;
	@Autowired
	private NotifyRefActiviryService notifyRefActiviryService;
	protected static final String ACTIVITY_STATUS_UNCLAIMED = ReadConfig.getUcpFieldValue("activity_status_unclaimed");
	protected static final String RELEASE_STATUS_UNRELEASED = ReadConfig.getUcpFieldValue("release_status_unreleased");
	private static final String ACTIVITY_ISSUETYPE_ID = ReadConfig.getUcpFieldValue("activityIssueTypeId");
	/**
	 * 活动状态-测试通过
	 */
	protected static final String ACTIVITY_STATUS_TEST_PASS = ReadConfig.getUcpFieldValue("activity_status_test_pass");
	protected static final String ACTIVITY_STATUS_REJECT = ReadConfig.getUcpFieldValue("activity_status_reject");

	@RequestMapping(method = RequestMethod.POST)
	public NotityResponse notify(NotityRequest notityRequest,
			@RequestParam("fileContent") MultipartFile[] fileContent) {

		NotityResponse response = new NotityResponse();
		try {

			// {"activityId":"KFC202105192170","event":"4","status":"WAITING_DR_FINANCE_CONFIRM"}

			log.info("request---={}", JSONObject.toJSONString(notityRequest));
			checkParam(notityRequest);
			if (notityRequest.getEvent().equals(Event.add.getCode())) {
				add(notityRequest, fileContent);
			} else if (notityRequest.getEvent().equals(Event.revocation.getCode())) {
				revocation(notityRequest);
			} else if (notityRequest.getEvent().equals(Event.reject.getCode())) {
				reject(notityRequest);
			} else if (notityRequest.getEvent().equals(Event.sync.getCode())) {
				sync(notityRequest);
			}

			response = NotityResponse.success();

		} catch (NotifyException e) {
			log.error("notify业务错误", e.getErrorMsg());
			response = NotityResponse.fail(e.getErrorCode(), e.getErrorMsg());
		} catch (Exception e) {
			log.error("", e);
			response = NotityResponse.fail(NotifyErrorCode.system_error.getCode(),
					NotifyErrorCode.system_error.getDesc());
		}
		log.info("resp---={}", JSONObject.toJSONString(notityRequest));
		return response;
	}

	private void sync(NotityRequest notityRequest) {
		/**
		 * 根据参数查出通知对象并校验
		 */
		List<NotifyActivity> notifyActivityList = notifyActivityService.findByActivityId(notityRequest.getActivityId());
		if (notifyActivityList == null || notifyActivityList.size() < 1) {
			throw new NotifyException(NotifyErrorCode.not_reject);
		}

		NotifyActivity notifyActivity = notifyActivityList.get(0);

		notifyActivity.setStatus(NotifyActivityStatus.getStatusByOutCode(notityRequest.getStatus()));
		notifyActivityService.save(notifyActivity);

	}

	/**
	 * 退回
	 * 
	 * @param notityRequest
	 */
	private void reject(NotityRequest notityRequest) {

		/**
		 * 根据参数查出通知对象并校验
		 */
		List<NotifyActivity> notifyActivityList = notifyActivityService.findByActivityId(notityRequest.getActivityId());
		if (notifyActivityList == null || notifyActivityList.size() < 1) {
			if (!NotifyActivityStatus.isReject(notifyActivityList.get(0).getStatus())) {
				throw new NotifyException(NotifyErrorCode.not_reject);
			}
		}

		/**
		 * 根据通知对象查出关联关系表并校验
		 */
		NotifyActivity notifyActivity = notifyActivityList.get(0);
		NotifyRefActivity notifyRefActivity = notifyRefActiviryService.findByNotifyId(notifyActivity.getId());

		if (notifyRefActivity == null) {
			throw new NotifyException(NotifyErrorCode.not_reject);
		}

		/**
		 * 根据关联关系表查出activity表并校验
		 */
		Activity activity = activityService.get(notifyRefActivity.getActivityId());
		if (activity == null) {
			throw new NotifyException(NotifyErrorCode.not_reject);
		}
		if (!ACTIVITY_STATUS_TEST_PASS.equals(activity.getStatus())) {
			log.info("activity status error={}", JSONObject.toJSONString(activity));
			throw new NotifyException(NotifyErrorCode.status_error);
		}

		/**
		 * 修改activity状态为退回，通知状态为退回
		 */
		if (NotifyRoleEnum.COUPON_ACTIVITY_APPLY_ROLE.getCode().equals(notityRequest.getRole())) {
			notifyActivity.setStatus(NotifyActivityStatus.mkt_reject.getCode());
		} else if (NotifyRoleEnum.COUPON_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_DR_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_B2_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_B1_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_CSSC_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_PRIME_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_VFC_B1_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_VFC_B2_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())) {
			notifyActivity.setStatus(NotifyActivityStatus.finance_reject.getCode());

		} else if (NotifyRoleEnum.COUPON_LEGAL_AUDIT_ROLE.getCode().equals(notityRequest.getRole())) {
			notifyActivity.setStatus(NotifyActivityStatus.legal_reject.getCode());
		} else {
			throw new NotifyException(NotifyErrorCode.param_error.getCode(), "无可匹配的角色");
		}
		activity.setReleaseStatus(RELEASE_STATUS_UNRELEASED);
		activity.setStatus(ACTIVITY_STATUS_UNCLAIMED);
		activity.setJiraStatus(ACTIVITY_ISSUETYPE_ID);
		activity.setIsNewRecord(false);
		activity.setReceiveUser(null);
		activityService.save(activity);
		notifyActivityService.save(notifyActivity);
		List<UcpTask> ucpTaskList = ucpTaskService.getByActId(activity.getId());
		if (!CollectionUtils.isEmpty(ucpTaskList)) {
			for (UcpTask ucpTask : ucpTaskList) {
				ucpTask.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
				ucpTaskService.delete(ucpTask);
				UcpTaskReceive ucpTaskReceive = ucpTaskReceiveService.getByTaskId(ucpTask.getId());
				if (ucpTaskReceive != null) {
					ucpTaskReceive.setDelFlag(BaseEntity.DEL_FLAG_DELETE);
					ucpTaskReceiveService.delete(ucpTaskReceive);
				}
			}

		}

	}

	/**
	 *
	 * @param notityRequest
	 * @return
	 */
	private void revocation(NotityRequest notityRequest) {

		List<NotifyActivity> notifyActivityList = notifyActivityService
				.findByActivityIdAndStatus(notityRequest.getActivityId(), NotifyActivityStatus.wait.getCode());
		if (notifyActivityList == null || notifyActivityList.size() < 1) {
			throw new NotifyException(NotifyErrorCode.activity_accept);
		}
		NotifyActivity notifyActivity = notifyActivityList.get(0);
		notifyActivity.setIsNewRecord(false);
		if (NotifyRoleEnum.COUPON_ACTIVITY_APPLY_ROLE.getCode().equals(notityRequest.getRole())) {
			notifyActivity.setStatus(NotifyActivityStatus.mkt_revocation.getCode());
		} else if (NotifyRoleEnum.COUPON_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_DR_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_B2_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_B1_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_CSSC_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_PRIME_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_VFC_B1_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())
				|| NotifyRoleEnum.COUPON_VFC_B2_FINANCE_AUDIT_ROLE.getCode().equals(notityRequest.getRole())) {
			notifyActivity.setStatus(NotifyActivityStatus.finance_revocation.getCode());
		} else if (NotifyRoleEnum.COUPON_LEGAL_AUDIT_ROLE.getCode().equals(notityRequest.getRole())) {
			notifyActivity.setStatus(NotifyActivityStatus.legal_revocation.getCode());
		} else {
			throw new NotifyException(NotifyErrorCode.param_error.getCode(), "无可匹配的角色");
		}
		notifyActivityService.save(notifyActivity);
	}

	private void add(NotityRequest notityRequest, MultipartFile[] fileContent) throws Exception {
		List<NotifyActivity> notifyActivityList = notifyActivityService
				.findByActivityIdAndStatus(notityRequest.getActivityId(), NotifyActivityStatus.wait.getCode());
		if (notifyActivityList != null && notifyActivityList.size() > 0) {
			throw new NotifyException(NotifyErrorCode.activity_repetition);
		}
		notifyActivityService.deleteByActivityId(notityRequest.getActivityId());

		NotifyActivity notifyActivity = new NotifyActivity();
		notifyActivity.setActivityId(notityRequest.getActivityId());
		notifyActivity.setActivityName(notityRequest.getActivityName());
		notifyActivity.setStatus(NotifyActivityStatus.wait.getCode());
		notifyActivity.setCampaignCode(notityRequest.getCampaignCode());
		List<NotifyFile> notifyFileList = new ArrayList<>();
		for (MultipartFile file : fileContent) {
			String fileUrl = saveExcelContent(file, file.getOriginalFilename(),
					String.valueOf(SnowFlakeUtils.getNextId()));
			NotifyFile entity = new NotifyFile();
			entity.setFileName(file.getOriginalFilename());
			entity.setFileUrl(fileUrl);
			entity.setId(IdGen.uuid());
			entity.setCreateDate(new Date());
			notifyFileList.add(entity);
		}
		notifyActivity.setSummary(notityRequest.getSummary());
		notifyActivity.setBrand(notityRequest.getBrand());
		notifyActivity.setCouponType(notityRequest.getCouponType());
		notifyActivity.setIsUrgent(notityRequest.getIsUrgent());
		notifyActivity.setProposer(notityRequest.getProposer());
		if (StringUtils.isNotEmpty(notityRequest.getUrgentDate())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			notifyActivity.setUrgentDate(sdf.parse(notityRequest.getUrgentDate()));
		}
		notifyActivityService.save(notifyActivity);

		for (NotifyFile notifyFile : notifyFileList) {
			notifyFile.setNotifyId(notifyActivity.getId());
			notifyFileService.insert(notifyFile);
		}

	}

	private void checkParam(NotityRequest notityRequest) {
		if (notityRequest == null) {
			throw new NotifyException(NotifyErrorCode.param_error.getCode(), "参数为空");
		}
		if (StringUtils.isEmpty(notityRequest.getEvent())) {
			throw new NotifyException(NotifyErrorCode.param_error.getCode(), "event为空");
		}
		if (StringUtils.isEmpty(notityRequest.getActivityId())) {
			throw new NotifyException(NotifyErrorCode.param_error.getCode(), "activityId为空");
		}
	}

	private String saveExcelContent(MultipartFile excelContent, String fName, String path) throws Exception {
		JSONObject jsonObject = FtpUtils.uploadSignalFileByFileNameAndFilePath(excelContent.getInputStream(), fName,
				path);
		if (jsonObject.get("filePath") == null) {
			throw new NotifyException(NotifyErrorCode.upload_error);
		}
		return jsonObject.getString("filePath");
	}

}
