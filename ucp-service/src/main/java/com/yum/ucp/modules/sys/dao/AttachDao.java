/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yum.ucp.modules.sys.dao;

import java.util.List;

import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.sys.entity.Attach;
import org.apache.ibatis.annotations.Param;

/**
 * 附件DAO接口
 * @author Zachary
 * @version 2019-08-15
 */
@MyBatisDao
public interface AttachDao extends CrudDao<Attach> {
	/**
	 *
	 * @param attach
	 * @return
	 */
	List<Attach> findListByClassAndType(Attach attach);

	/**
	 * 查询单个的附件
	 * @param attach
	 * @return
	 */
	Attach getByClassNameAndClassPk(Attach attach);

	/**
	 * 查询单个的附件
	 * @param filePath
	 * @return
	 */
	Attach getByFilePath(@Param("filePath") String filePath);

	/**
	 * 查询单个的附件
	 * @param fileName 文件名是由雪花算法生成的，是唯一的
	 * @return
	 */
	Attach getByFileName(@Param("fileName") String fileName);

	/**
	 * 彻底删除附件
	 * @param attach
	 * @return
	 */
	void deleteByFilePath(Attach attach);

	/**
	 * 根据分类删除附件
	 * @param attach
	 */
	void deleteByClassNameAndClassPkAndType(Attach attach);
}