/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.dao;

import com.yum.ucp.common.persistence.TreeDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.sys.entity.Dict;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典DAO接口
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface DictDao extends TreeDao<Dict> {

	 List<String> findTypeList(Dict dict);

	void updateDict(Dict dict);

	void updateDictIsSelect(@Param("id") String id,@Param("parentId") String parentId);

	Dict getByValue(String value);

	List<Dict> findByParentId(String parentId);

	List<Dict> findByParentIds(@Param("parentIds") String parentIds);

	List<Dict> findByParentIdAndValue(@Param("parentId") String parentId,@Param("value") String value);

	public void deleteDict();

	public String findByValue(String id,String parentId);
	
	Dict getByTypeAndValue(@Param("type") String type, @Param("value") String value);
}
