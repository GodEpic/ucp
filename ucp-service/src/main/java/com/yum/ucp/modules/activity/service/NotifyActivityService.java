package com.yum.ucp.modules.activity.service;

import com.alibaba.fastjson.JSONObject;
import com.yum.ucp.common.config.Global;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.common.utils.HttpClientUtil;
import com.yum.ucp.common.utils.IdGen;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.activity.dao.NotifyActivityDao;
import com.yum.ucp.modules.activity.entity.Activity;
import com.yum.ucp.modules.activity.entity.NotifyActivity;
import com.yum.ucp.modules.activity.entity.NotifyRefActivity;
import com.yum.ucp.modules.activity.http.NotifyInterfaceService;
import com.yum.ucp.modules.enumerate.NotifyActivityStatus;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotifyActivityService extends CrudService<NotifyActivityDao, NotifyActivity> {

    @Autowired
    private NotifyRefActiviryService notifyRefActiviryService;



    private String notifyRejectUrl= Global.getConfig(Global.NOTIFY_REJECT_URL);

    /**
     * 根据activityId和status查询
     * @param activityId
     * @param status
     * @return
     */
    public List<NotifyActivity> findByActivityIdAndStatus(String activityId, String status){
        NotifyActivity query=new NotifyActivity();
        query.setActivityId(activityId);
        query.setStatus(status);
        return dao.findByActivityIdAndStatus(query);
    }


    /**
     * 根据activityId和status查询
     * @param activityId
     * @return
     */
    public List<NotifyActivity> findByActivityId(String activityId){
        NotifyActivity query=new NotifyActivity();
        query.setActivityId(activityId);
        return dao.findByActivityId(query);
    }

    /**
     * 根据activityId更新status的值
     * @param activityId
     * @return
     */
    @Transactional(readOnly = false)
    public void updateStatusById(String activityId,String status){
        NotifyActivity query=new NotifyActivity();
        query.setActivityId(activityId);
        query.setStatus(status);
        dao.updateStatusById(query);
    }
    /**
     * 接受
     * @param activityId
     * @param notivityActivityId
     */
    public void accept(String activityId,String notivityActivityId){
        if(notifyRefActiviryService.findByNotifyId(notivityActivityId)==null) {
            NotifyRefActivity notifyRefActivity = new NotifyRefActivity();
            notifyRefActivity.setNotifyId(notivityActivityId);
            notifyRefActivity.setActivityId(activityId);
            notifyRefActiviryService.save(notifyRefActivity);
        }
        if(StringUtils.isNotBlank(notivityActivityId)) {
            NotifyActivity notifyActivity = dao.get(notivityActivityId);
            if(notifyActivity!=null) {
                notifyActivity.setStatus(NotifyActivityStatus.accept.getCode());
                notifyActivity.preUpdate();
                dao.update(notifyActivity);
            }
        }

    }


    /**
     * 撤回
     * @param notivifyId
     */
    @Transactional(readOnly = false)
    public void revocation(String notivifyId){
        NotifyActivity notifyActivity=dao.get(notivifyId);
        if(notifyActivity!=null&&notifyActivity.getStatus().equals(NotifyActivityStatus.wait.getCode())) {
            reject(notifyActivity);

            notifyActivity.setStatus(NotifyActivityStatus.ucp_reject.getCode());
            notifyActivity.setIsNewRecord(false);
            notifyActivity.preUpdate();
            dao.update(notifyActivity);
        }
    }


    /**
     * 退回
     * @param notifyActivity
     */
    public void reject(NotifyActivity notifyActivity){
        String respStr= HttpClientUtil.doGet(notifyRejectUrl+notifyActivity.getActivityId());
        JSONObject resp=JSONObject.parseObject(respStr);
        if(resp.getInteger("code")==0){
            return;
        }else{
            logger.error("notify error=="+respStr);
        }
    }


    /**
     * 还原
     */
    @Transactional(readOnly = false)
    public void reduction(Activity activity){
        List<NotifyRefActivity> list=notifyRefActiviryService.findByActivityId(activity.getId());
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for (NotifyRefActivity ref:list) {
            NotifyActivity notifyActivity=get(ref.getNotifyId());
            notifyActivity.setStatus(NotifyActivityStatus.wait.getCode());
            notifyActivity.preUpdate();
            dao.update(notifyActivity);
            notifyRefActiviryService.delete(ref);

        }


    }

    /**
     * 删除历史记录
     * @param activityId
     * @return
     */
    @Transactional(readOnly = false)
    public void deleteByActivityId(String activityId){
        NotifyActivity query=new NotifyActivity();
        query.setActivityId(activityId);
         dao.deleteByActivityId(query);
    }






}
