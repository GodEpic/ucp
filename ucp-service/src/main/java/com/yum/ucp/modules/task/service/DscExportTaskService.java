package com.yum.ucp.modules.task.service;

import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.sys.utils.UploadFileUtils;
import com.yum.ucp.modules.task.dao.DscExportTaskDao;
import com.yum.ucp.modules.task.entity.DscExportTask;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;

@Service
@Transactional(readOnly = false)
public class DscExportTaskService extends CrudService<DscExportTaskDao, DscExportTask> {


    public List<DscExportTask> findEtGExportTask() {
        List<DscExportTask> etGExportTasks = dao.findDscExportTask();
        return etGExportTasks;
    }

    public List<DscExportTask> getUnFinishedExportTask() {
        List<DscExportTask> etGExportTasks = dao.getUnFinishedExportTask();
        return etGExportTasks;
    }

    @Transactional(readOnly = false)
    public void updateExportTaskStatus(String id,int status){
        dao.updateExportTaskStatus(id,status);
    }

    @Transactional(readOnly = false)
    public void finishTask(DscExportTask dscExportTask){
        dao.finishTask(dscExportTask);
    }


    public void downloadfile(String saveName, OutputStream os,DscExportTask file)
            throws FileNotFoundException {
        UploadFileUtils.downloadfile(saveName, os);
    }
}
