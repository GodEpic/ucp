/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.service;

import com.yum.ucp.common.service.TreeService;
import com.yum.ucp.common.utils.CacheUtils;
import com.yum.ucp.modules.sys.constants.CommonConstants;
import com.yum.ucp.modules.sys.dao.DictDao;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 字典Service
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class DictService extends TreeService<DictDao, Dict> {
	
	/**
	 * 查询字段类型列表
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}

	public List<Dict> findListByParentId(String parentId){
		return dao.findByParentId(parentId);
	}
	@Transactional(readOnly = false)
	public void save(Dict dict) {
		super.save(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	@Transactional(readOnly = false)
	public void updateDict(Dict dict) {
		//删除期他固定值
		if(dict.getIsSelect().equals(CommonConstants.DICT_SELECT.YES)){
			dao.updateDictIsSelect(dict.getId(),dict.getParentId());
		}
		  dao.updateDict(dict);
	}

	@Transactional(readOnly = false)
	public void delete(Dict dict) {
		super.delete(dict);
		CacheUtils.remove(DictUtils.CACHE_DICT_MAP);
	}

	public String findByValue(String id,String parentId){
		return dao.findByValue(id,parentId);
	}

	@Override
	public List<Dict> findList(Dict dict) {
		dict.setParentIds(dict.getParentIds()+"%");
		return dao.findByParentIdsLike(dict);
	}

	public List<Dict> findAllList(Dict dict) {
		return dao.findAllList(dict);
	}
}
