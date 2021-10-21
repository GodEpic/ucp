/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.dao;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.sys.entity.DscConfigLine;
import com.yum.ucp.modules.sys.entity.DscHotLine;
import com.yum.ucp.modules.sys.entity.DscHotLineMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * jira用户DAO接口
 * @author Edward.Luo
 * @version 2017-01-19
 */
@MyBatisDao
public interface DscHotLineDao extends CrudDao<DscHotLine> {

    public List<String> findMenuIdById(String id);
    DscHotLine  getRoleByNum (String num);

    public List<String> findConfigIdById(String id);

    public void insertBatchMenu(@Param("dscHotLineMenus")List<DscHotLineMenu> dscHotLineMenus);

    public void insertBatchConfig(@Param("dscConfigLines")List<DscConfigLine> dscConfigLines);

    public void deleteById(String id);

}