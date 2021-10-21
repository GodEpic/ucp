/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.sys.dao.DscHotLineDao;
import com.yum.ucp.modules.sys.entity.DscConfigLine;
import com.yum.ucp.modules.sys.entity.DscHotLine;
import com.yum.ucp.modules.sys.entity.DscHotLineMenu;
import com.yum.ucp.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * jira用户Service
 *
 * @author Edward.Luo
 * @version 2017-01-19
 */
@Service
@Transactional(readOnly = true)
public class DscHotLineService extends CrudService<DscHotLineDao, DscHotLine> {

    public DscHotLine get(String id) {
        return super.get(id);
    }
    public DscHotLine getRoleByNum(String num) {
        return dao.getRoleByNum(num);
    }


    public List<DscHotLine> findList(DscHotLine dscUserJira) {
        return super.findList(dscUserJira);
    }

    public Page<DscHotLine> findPage(Page<DscHotLine> page, DscHotLine dscUserJira) {
        return super.findPage(page, dscUserJira);
    }

    @Transactional(readOnly = false)
    public void delete(DscHotLine dscUserJira) {
        super.delete(dscUserJira);
    }


    public List<String> findMenuIdById(String id) {
        return dao.findMenuIdById(id);
    }

    public List<String> findConfigIdById(String id) {
        return dao.findConfigIdById(id);
    }
    /**
     * 保存数据（插入或更新）
     *
     * @param entity
     */
    @Transactional(readOnly = false)
    public void save(DscHotLine entity) {
        List<DscHotLineMenu> dscHotLineMenus = new ArrayList<>();
        List<DscConfigLine> dscConfigLines = new ArrayList<>();
        List<String> menuIds = entity.getMenuIds();
        List<String> configIds = entity.getConfigIds();
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            dao.insert(entity);

        } else {
            entity.preUpdate();
            dao.update(entity);
        }
        dao.deleteById(entity.getId());
        for (String str : menuIds) {
            DscHotLineMenu dscHotLineMenu = new DscHotLineMenu();
            dscHotLineMenu.setHotLineId(entity.getId());
            dscHotLineMenu.setMenuId(str);
            dscHotLineMenus.add(dscHotLineMenu);
        }
        if(dscHotLineMenus.size()>0) {
            dao.insertBatchMenu(dscHotLineMenus);
        }
            for (String str : configIds) {
                DscConfigLine dscConfigLine = new DscConfigLine();
                dscConfigLine.setHotLineId(entity.getId());
                dscConfigLine.setConfigId(str);
                dscConfigLines.add(dscConfigLine);
            }
        if(dscConfigLines.size()>0) {
            dao.insertBatchConfig(dscConfigLines);
        }
        UserUtils.removeCache(UserUtils.CACHE_HOT_LINE_LIST);
    }
}