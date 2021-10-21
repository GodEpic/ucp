package com.yum.ucp.modules.task.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.task.entity.DscExportTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 此类为[代码工厂]自动生成，继承了BaseDao类，已经拥有基本的增删改成操作
 * <其他请自行扩展>
 *
 * @time 2016-05-05 10:35
 * @CodeFactoryGenerated
 */
@MyBatisDao
public interface DscExportTaskDao extends CrudDao<DscExportTask> {


    void updateExportTaskStatus(@Param("id") String id, @Param("status") int status);

    void finishTask(DscExportTask dscExportTask);

    List<DscExportTask> findDscExportTask();

    List<DscExportTask> getUnFinishedExportTask();
}