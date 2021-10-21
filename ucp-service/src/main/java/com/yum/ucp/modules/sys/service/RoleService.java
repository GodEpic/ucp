/**
 * Copyright &copy; 2012-2013 <a href="httparamMap://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import com.yum.ucp.common.persistence.Page;
import com.yum.ucp.common.service.CrudService;
import com.yum.ucp.modules.sys.dao.RoleDao;
import com.yum.ucp.modules.sys.entity.Role;
import com.yum.ucp.modules.ztree.NodeBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志Service
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class RoleService extends CrudService<RoleDao, Role> {

    public Page<Role> findPage(Page<Role> page, Role Role) {
        return super.findPage(page, Role);
    }

    public List<String> findByUserId(String userId) {

        return dao.findByUserId(userId);

    }

    public void updateMenu(Role entity) {
        dao.deleteRoleMenu(entity);
        if(null!=entity.getMenuList()&&entity.getMenuList().size()>0){
            dao.insertRoleMenu(entity);
        }

    }
    public Role getRole(Role entity) {
        return dao.getRole(entity);
    }
    public Integer findCountByRoleId(Role entity) {
        return dao.findCountByRoleId(entity);
    }

    /**
     * 根据角色类型查询角色树
     *
     * @param roleType
     * @return
     */
    public List<NodeBean> getRoleByWorkCode(String roleType) {
        Role entity = new Role();
        entity.setRoleType(roleType);
        //查询出所有有效的角色列表
        List<Role> roles = dao.findListByRoleLevel(entity);
        List<NodeBean> tree = new ArrayList<NodeBean>();
        NodeBean node = null;
        for (Role role : roles) {
            node = new NodeBean(role.getId(), "0", role.getName(), "", false);
            tree.add(node);
        }
        return tree;
    }
}
