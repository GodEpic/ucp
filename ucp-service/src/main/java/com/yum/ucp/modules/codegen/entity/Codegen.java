package com.yum.ucp.modules.codegen.entity;


import com.yum.ucp.common.persistence.DataEntity;

/**
 * 序列号
 * 
 * @author  user
 * @version  [版本号, 2015年10月10日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class Codegen extends DataEntity<Codegen> {

    private static final long serialVersionUID = -3810600618837495200L;

    private String systemCode;//系统编码
    
    private String category;//分类
    
    private String name;//描述
    
    private String format;//表达式
    
    private String uniqueFormat;//唯一值表达式值
    
    private String remark;//备注
    
    public String getCategory()
    {
        return category;
    }
    
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getFormat()
    {
        return format;
    }
    
    public void setFormat(String format)
    {
        this.format = format;
    }

    
    public String getRemark()
    {
        return remark;
    }
    
    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    public String getSystemCode()
    {
        return systemCode;
    }
    
    public void setSystemCode(String systemCode)
    {
        this.systemCode = systemCode;
    }

    public String getUniqueFormat() {
        return uniqueFormat;
    }

    public void setUniqueFormat(String uniqueFormat) {
        this.uniqueFormat = uniqueFormat;
    }
}
