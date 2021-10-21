package com.yum.ucp.modules.sys.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yum.ucp.common.utils.JedisUtils;
import com.yum.ucp.common.utils.JsonUtils;
import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.common.entity.CommonSelect;
import com.yum.ucp.modules.sys.dao.DictDao;
import com.yum.ucp.modules.sys.entity.Dict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/29.
 */
public class SelectDataInit implements ServletContextListener {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    private static DictDao dictDao;
    private static final String SELECT_DATA = "selectData";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        this.initSelect(servletContext);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public void initSelect(ServletContext servletContext) {
        try {
            dictDao = SpringContextHolder.getBean(DictDao.class);
            CommonSelect common;
            List<CommonSelect> commonSelects;

            Jedis jedis = null;
            jedis = JedisUtils.getResource();
            Map<String, String> map = jedis.hgetAll("selectData");
            if (map.isEmpty()) {
                /**
                 * 性别
                 */
                List<CommonSelect> genders = new ArrayList<CommonSelect>();
                Dict dict = dictDao.getByValue("gender");
                if (null != dict) {
                    List<Dict> dictList = dictDao.findByParentId(dict.getId());
                    genders.add(new CommonSelect("", "请选择", "", ""));
                    for (Dict d : dictList) {
                        genders.add(new CommonSelect(d.getValue(), d.getLabel(), d.getIsSelect(), d.getDescription()));
                    }
                }
                jedis.hset(SELECT_DATA, "genders", JsonUtils.stringify(genders));
                servletContext.setAttribute("genders", genders);

            } else {
                for (Map.Entry<String, String> e : map.entrySet()){
                    List<CommonSelect> list = JsonUtils.parseList(e.getValue(), new TypeReference<List<CommonSelect>>() {});
                    servletContext.setAttribute(e.getKey(), list);
                }
            }
        } catch (Exception e) {
            logger.error("SelectDataInit error:", e);
        }

    }
}
