/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.sys.dao.DscHotLineConfigDao;
import com.yum.ucp.modules.sys.entity.DscHotLineConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * jira用户Service
 *
 * @author Edward.Luo
 * @version 2017-01-19
 */
@Service
@Transactional(readOnly = true)
public class DscHotLineConfigService extends CrudService<DscHotLineConfigDao, DscHotLineConfig> {

    public DscHotLineConfig get(String id) {
        return super.get(id);
    }

    public List<DscHotLineConfig> findList(DscHotLineConfig dscHotLineConfig) {
        return super.findList(dscHotLineConfig);
    }

    public Page<DscHotLineConfig> findPage(Page<DscHotLineConfig> page, DscHotLineConfig dscHotLineConfig) {
        return super.findPage(page, dscHotLineConfig);
    }

    @Transactional(readOnly = false)
    public void delete(DscHotLineConfig dscHotLineConfig) {
        super.delete(dscHotLineConfig);
    }

    public List<String> findListByHotLine(String hotLine) {
        return dao.findListByHotLine(hotLine);
    }

}