package com.yum.ucp.common.utils;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2017/4/12.
 */
public class JiraRestClientObjListener implements RemovalListener<String,JiraRestClient> {

    /**
     * 日志对象
     */
    public org.slf4j.Logger logger = LoggerFactory.getLogger(JiraRestClientObjListener.class);
    @Override
    public void onRemoval(RemovalNotification<String, JiraRestClient> notification) {
        JiraRestClient jiraRestClient = notification.getValue();
        try {
            if(jiraRestClient!=null){
                jiraRestClient.close();
                logger.info("jiraRestClient onRemoval close();  ");
            }
        } catch (IOException e) {
            logger.error("error message:{}", e.getMessage());
        }
    }
}
