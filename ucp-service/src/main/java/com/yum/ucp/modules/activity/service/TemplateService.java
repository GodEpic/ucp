/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yum.ucp.modules.activity.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.activity.dao.TemplateDao;
import com.yum.ucp.modules.activity.entity.Template;
import com.yum.ucp.modules.activity.pojo.ActivityVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动模块Service
 *
 * @author tony
 * @version 2019-07-26
 */
@Service
@Transactional(readOnly = true)
public class TemplateService extends CrudService<TemplateDao, Template> {

    @Override
    public Page<Template> findPage(Page<Template> page, Template template) {
        return super.findPage(page, template);
    }

    public Integer getLastValueSeq() {
        return dao.getLastValueSeq();
    }
}
