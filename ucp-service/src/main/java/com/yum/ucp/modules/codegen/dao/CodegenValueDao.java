package com.yum.ucp.modules.codegen.dao;


import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.codegen.entity.CodegenValue;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 序列号的数据库访问类
 * 
 * @author user
 * @version [版本号, 2015年10月10日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@MyBatisDao
public interface CodegenValueDao extends CrudDao<CodegenValue>
{

    public CodegenValue getCodegenValue(@Param("uniqueKey") String uniqueKey, @Param("codegenId") String codegenId);

    int updateCodegenValue(@Param("id") String id, @Param("vals") String vals, @Param("version") int version
            , @Param("userId") String userId, @Param("date") Date date);
    
}
