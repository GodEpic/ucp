package com.yum.ucp.modules.activity.http;

import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.utils.HttpClientUtil;
import com.yum.ucp.modules.activity.dao.ActivityDao;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.activity.entity.NotifyActivity;
import com.yum.ucp.modules.activity.entity.NotifyRefActivity;
import com.yum.ucp.modules.activity.entity.QaReport;
import com.yum.ucp.modules.activity.service.ActivityService;
import com.yum.ucp.modules.activity.service.NotifyActivityService;
import com.yum.ucp.modules.activity.service.NotifyRefActiviryService;
import com.yum.ucp.modules.activity.service.QaReportService;
import com.yum.ucp.modules.enumerate.NotifyActivityStatus;
import com.yum.ucp.modules.sys.entity.Attach;
import com.yum.ucp.modules.sys.service.AttachService;
import com.yum.ucp.modules.sys.utils.ReadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotifyInterfaceService {

    Logger logger=LoggerFactory.getLogger(this.getClass());
    @Autowired
    NotifyActivityService notifyActivityService;
    @Autowired
    NotifyRefActiviryService notifyRefActiviryService;
    @Autowired
    AttachService attachService;
    @Autowired
    QaReportService qaReportService;
    @Autowired
    private ActivityService activityService;


    private String notifySuccessUrl= Global.getConfig(Global.NOTIFY_SUCCESS_URL);


    /**
     * 通知成功
     * @param activity
     */
    public void notivifySuccess(Activity activity){
        List<NotifyRefActivity> notifyRefActivityList =notifyRefActiviryService.findByActivityId(activity.getId());
        if(!CollectionUtils.isEmpty(notifyRefActivityList)){
            for (NotifyRefActivity notifyRefActivity:notifyRefActivityList){
                NotifyActivity notifyActivity=notifyActivityService.get(notifyRefActivity.getNotifyId());
                if(notifyActivity.getStatus().equals(NotifyActivityStatus.accept.getCode())){
                    httpNotify(activity,notifyActivity);
                    break;
                }
            }
        }

    }




    /**
     * 请求通知
     * @param activity
     * @param notifyActivity
     */
    private void httpNotify(Activity activity, NotifyActivity notifyActivity) {
        JSONObject req=new JSONObject();
        Attach query=new Attach();
        query.setDelFlag("0");
        query.setClassPk(activity.getId());
        req.put("activityId",notifyActivity.getActivityId());

        List<Attach> list=attachService.findListByClassAndType(query);

        QaReport qaReport =qaReportService.getByActId(activity.getId());

        if(qaReport!=null){
            query.setClassPk(qaReport.getId());
            List<Attach> list2=attachService.findListByClassAndType(query);
            list.addAll(list2);
        }
        if(!CollectionUtils.isEmpty(list)) {
            List<Map> maplist=new ArrayList<>();
            for (Attach attach:list){
                Map<String,Object> map=new HashMap<>();
                map.put("filePath",attach.getFilePath());
                map.put("fileName",attach.getFileName());
                maplist.add(map);
            }
            req.put("fileList",maplist);
        }

        logger.info("http request---{},{}",notifySuccessUrl,req.toJSONString());
        String respStr=HttpClientUtil.doPostJson(notifySuccessUrl,req.toJSONString());
        logger.info("http response---{}",respStr);
        JSONObject resp=JSONObject.parseObject(respStr);
        if(resp.getInteger("code")==0){
            String status=NotifyActivityStatus.getStatusByOutCode(resp.getString("status"));
            notifyActivityService.updateStatusById(notifyActivity.getActivityId(),status);
            logger.info("activityID :"+notifyActivity.getActivityId()+" was updated, status = "+status);
            return;
        }else{
            logger.error("notify error code--{}",resp.getInteger("code"));
        }
    }


//    public static void main(String[] args) {
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("activityId","123");
//        jsonObject.put("fileUrl","sdad");
//        String resp=HttpClientUtil.doPostJson("http://172.25.221.188:8098/eap-fast/activity/activiynotify",jsonObject.toJSONString());
//        System.out.println(resp);
//    }
}
