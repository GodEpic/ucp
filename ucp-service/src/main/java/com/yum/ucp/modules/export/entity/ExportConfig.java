package com.yum.ucp.modules.export.entity;

import com.yum.ucp.common.persistence.DataEntity;

/**
 * exportConfig 导出配置
 *
 * @author tony
 * @version 2019-04-18
 */
public class ExportConfig extends DataEntity<ExportConfig> {
    private static final long serialVersionUID = 1L;

    private String name;

    private String exportSql;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExportSql() {
        return exportSql;
    }

    public void setExportSql(String exportSql) {
        this.exportSql = exportSql;
    }
}
