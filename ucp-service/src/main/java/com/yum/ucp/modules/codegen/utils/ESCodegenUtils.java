package com.yum.ucp.modules.codegen.utils;


import com.google.common.collect.Lists;
import com.yum.ucp.common.service.ServiceException;
import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.codegen.service.CodegenService;

import java.util.List;

/**
 * Created by Eddy.Xu on 2016/12/15.
 */
public class ESCodegenUtils {

    private static CodegenService codegenService = SpringContextHolder.getBean(CodegenService.class);



    /**
     * 批量生成短信提取码
     * 提取码生成规则为YYYYMMDD+四位流水号，如生成后变更提取码个数，在已生成过的基础上，直接自增生成新的提取码。
     * @return
     */
    public static List<String> genServiceCodes(  int size, String userId){

        List<String> list = Lists.newArrayList();
        try {
            list = codegenService.genSmsGetCode(size, userId);
        } catch (ServiceException e) {
            e.printStackTrace();
            list.add("-1");
        }
        return list;
    }



}
