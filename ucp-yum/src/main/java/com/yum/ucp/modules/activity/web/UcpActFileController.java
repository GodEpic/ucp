/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.activity.web;

import com.yum.ucp.common.utils.IdGen;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.common.web.BaseController;
import com.yum.ucp.modules.activity.entity.UcpActFile;
import com.yum.ucp.modules.activity.service.UcpActFileService;
import com.yum.ucp.modules.sys.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 特殊活动附件关联Controller
 *
 * @author cherlin
 * @version 2019-08-23
 */
@Controller
@RequestMapping(value = "${adminPath}/ucpActFile")
public class UcpActFileController extends BaseController {

    @Autowired
    private UcpActFileService ucpActFileService;

    @ModelAttribute
    public UcpActFile get(@RequestParam(required = false) String id) {
        UcpActFile entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = ucpActFileService.get(id);
        }
        if (entity == null) {
            entity = new UcpActFile();
        }
        return entity;
    }

    @RequestMapping(value = "createActFile", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMessage createActFile() {
        UcpActFile actFile = new UcpActFile();
        actFile.setId(IdGen.uuid());
        actFile.setIsNewRecord(true);
        ucpActFileService.save(actFile);
        return ResponseMessage.success("保存成功", actFile.getId());
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public ResponseMessage delete(String id) {
        UcpActFile actFile = ucpActFileService.get(id);
        if (actFile != null) {
            ucpActFileService.delete(actFile);
        }
        return ResponseMessage.success("删除成功");
    }
}