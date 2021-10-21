/**
 * Copyright &copy; 2012-2016
 * <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yum.ucp.modules.export.service;

import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.export.dao.ExportConfigDao;
import com.yum.ucp.modules.export.entity.ExportConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * exportConfig 导出配置Service
 *
 * @author tony
 * @version 2019-04-18
 */
@Service
@Transactional(readOnly = true)
public class ExportConfigService extends CrudService<ExportConfigDao, ExportConfig> {

    @Autowired
    private ExportConfigDao exportConfigDao;

    public List<LinkedHashMap<String, Object>> findExportRecordList(String sql) {
        return exportConfigDao.findExportRecordList(sql);
    }

}
