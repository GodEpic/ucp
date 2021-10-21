package com.yum.ucp.modules.export.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.export.entity.ExportConfig;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * exportConfig 导出配置Dao接口
 *
 * @author tony
 * @version 2019-04-18
 */
@MyBatisDao
public interface ExportConfigDao extends CrudDao<ExportConfig> {
    /**
     * 导出传入SQL的查询结果
     * @param sql
     * @return
     */
    List<LinkedHashMap<String, Object>> findExportRecordList(@Param("sql") String sql);

}