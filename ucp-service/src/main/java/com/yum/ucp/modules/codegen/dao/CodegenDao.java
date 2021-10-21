package com.yum.ucp.modules.codegen.dao;


import com.yum.ucp.common.persistence.CrudDao;
import com.yum.ucp.common.persistence.annotation.MyBatisDao;
import com.yum.ucp.modules.codegen.entity.Codegen;
import org.apache.ibatis.annotations.Param;

/**
 * 序列号的数据库访问类
 * 
 * @author user
 * @version [版本号, 2015年10月10日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@MyBatisDao
public interface CodegenDao extends CrudDao<Codegen>
{

    public Codegen loadCodegen(@Param("systemCode") String systemCode, @Param("category") String category);
  /*
    *//**
     * see update
     *//*
    @Override
    public void update(Codegen po)
    {
        super.update(po);
    }
    
    *//**
     * see search
     *//*
    @Override
    public Page search(String systemCode, String category, Map<String, String> sort, int size, int start)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(Codegen.class);
        
        dc.add(Restrictions.eq("delf", CommonConstants.FLAG_UNDELETE));
        if (!StringUtils.isEmpty(systemCode))
        {
            dc.add(Restrictions.eq("systemCode", systemCode));
        }
        if (!StringUtils.isEmpty(category))
        {
            dc.add(Restrictions.like("category", "%" + category + "%"));
        }
        if (sort != null && !sort.isEmpty())
        {
            for (Entry<String, String> entry : sort.entrySet())
            {
                if (StringUtils.equals(CommonConstants.ORDER_ASC, entry.getValue()))
                {
                    dc.addOrder(Order.asc(entry.getKey()));
                }
                else
                {
                    dc.addOrder(Order.desc(entry.getKey()));
                }
            }
        }
        else
        {
            dc.addOrder(Order.desc("createTime"));
            dc.addOrder(Order.desc("id"));
        }
        return page(dc, size, start);
    }
    

    
    
    */
    public int count(@Param("category") String category);

    
}
