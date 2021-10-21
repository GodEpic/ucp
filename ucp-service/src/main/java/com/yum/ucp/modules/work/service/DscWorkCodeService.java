/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.work.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.common.utils.StringUtils;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.work.dao.DscRoleWorkCodeDao;
import com.yum.ucp.modules.work.dao.DscWorkCodeDao;
import com.yum.ucp.modules.work.entity.DscRoleWorkCode;
import com.yum.ucp.modules.work.entity.DscWorkCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 工号Service
 *
 * @author Edward.Luo
 * @version 2017-01-19
 */
@Service
@Transactional(readOnly = true)
public class DscWorkCodeService extends CrudService<DscWorkCodeDao, DscWorkCode> {

    @Autowired
    public DscRoleWorkCodeDao dscRoleWorkCodeDao;

    public DscWorkCode get(String id) {
        return super.get(id);
    }

    public List<String> getWorkCodeRole(String id) {
        List<DscRoleWorkCode> list = dscRoleWorkCodeDao.findByWorkCodeId(id);
        List<String> result = new ArrayList<String>();
        for (DscRoleWorkCode code : list) {
            result.add(code.getRoleId());
        }
        return result;
    }

    public List<DscWorkCode> findList(DscWorkCode dscWorkCode) {
        return super.findList(dscWorkCode);
    }

    public Page<DscWorkCode> findPage(Page<DscWorkCode> page, DscWorkCode dscWorkCode) {
        return super.findPage(page, dscWorkCode);
    }

    @Transactional(readOnly = false)
    public void save(DscWorkCode dscWorkCode) {
        super.save(dscWorkCode);
    }

    @Transactional(readOnly = false)
    public void delete(DscWorkCode dscWorkCode) {
        super.delete(dscWorkCode);
    }

    /**
     * 根据工号获取角色
     *
     * @param dscWorkCode
     */
    @Transactional(readOnly = false)
    public List<String> findRoleNamesByCode(String dscWorkCode) {
        return dao.findRoleNamesByCode(dscWorkCode);
    }

    /**
     * 根据工号获取角色公告
     *
     * @param dscWorkCode
     */
    @Transactional(readOnly = false)
    public List<String> findRoleAnnounceByCodeRoleType(String dscWorkCode, String roleType, String roleName) {
        return dao.findRoleAnnounceByCodeRoleType(roleType, roleName);
    }

    /**
     * 根据userId获取技能
     *
     * @param userId
     * @return
     */
    @Transactional(readOnly = false)
    public List<Dict> findRoleTypesByUserId(String userId) {
        return dao.findRoleTypesByUserId(userId);
    }

    /**
     * 绑定工号和角色的关系
     *
     * @param workCodeId
     * @param roleIds
     */
    @Transactional(readOnly = false)
    public void bindWorkCodeRole(String workCodeId, List<String> roleIds) {
        //每次保存时先执行删除操作将旧数据全部按照workId删除
        dao.deleteWorkCodeRole(workCodeId);
        //保存数据
        for (String roleId : roleIds) {
            dao.bindWorkCodeRole(roleId, workCodeId);
        }
    }

    /**
     * 检查工号和角色的绑定关系
     *
     * @param roleId
     * @param workCodeId
     * @return
     */
    public DscRoleWorkCode checkIfExist(String roleId, String workCodeId) {
        return dscRoleWorkCodeDao.checkIfExist(roleId, workCodeId);
    }

    /**
     * 查询用户关联的工号信息
     *
     * @param
     * @return
     */
    public Page<DscWorkCode> getUserWorkCode(Page<DscWorkCode> page, DscWorkCode dscWorkCode) {
        List<DscWorkCode> codes = dao.getUserWorkCode(dscWorkCode.getUser().getId());
        long count = dao.getUserWorkCodeCount(dscWorkCode.getUser().getId());
        dscWorkCode.setPage(page);
        page.setList(codes);
        page.setCount(count);
        return page;
    }

    @Transactional(readOnly = false)
    public void saveWorkCode(DscWorkCode workCode) {
        //获取工号绑定的角色信息
        List<String> roleIds = workCode.getRoleIdList();
        //判断是新增还是更新
        if (StringUtils.isNotEmpty(workCode.getId())) {
            //更新
            workCode.preUpdate();
            dao.update(workCode);
        } else {
            //保存工号信息
            workCode.preInsert();
            dao.insert(workCode);
        }
        //保存工号和角色关系之前将之前的关联关系全部清除
        dscRoleWorkCodeDao.deleteById(workCode.getId());
        //保存工号和角色的关联关系
        for (String roleId : roleIds) {
            DscRoleWorkCode roleWorkCode = new DscRoleWorkCode();
            roleWorkCode.setRoleId(roleId);
            roleWorkCode.setWorkCodeId(workCode.getId());
            dscRoleWorkCodeDao.insert(roleWorkCode);
        }

    }

    @Transactional(readOnly = false)
    public void deleteWorkCode(DscWorkCode workCode) {
        dao.delete(workCode);
        //关联关系全部清除
        dscRoleWorkCodeDao.deleteById(workCode.getId());
    }

    public DscWorkCode getByWorkCode(String workCode) {
        return dao.getByWorkCode(workCode);
    }

    public List<DscWorkCode> findUserIdsByRoleType(String ... roleType) {
        return dao.findUserIdsByRoleType(roleType);
    }
}