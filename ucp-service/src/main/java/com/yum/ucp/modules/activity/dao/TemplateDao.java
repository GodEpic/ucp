package com.yum.ucp.modules.activity.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.activity.entity.Template;

@MyBatisDao
public interface TemplateDao extends CrudDao<Template> {}
