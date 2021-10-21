package com.yum.ucp.modules.activity.utils;

import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.sys.dao.AttachDao;
import com.yum.ucp.modules.sys.entity.Attach;

import java.util.List;

public class AttachUtils {

    private static AttachDao attachDao = SpringContextHolder.getBean(AttachDao.class);

    public static List<Attach> findListByClassAndType(String className, String classPk, String type) {
        return attachDao.findListByClassAndType(new Attach(className,classPk,type));
    }
}
