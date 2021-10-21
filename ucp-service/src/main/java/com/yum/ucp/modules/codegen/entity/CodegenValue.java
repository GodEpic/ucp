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
public class CodegenValue extends DataEntity<CodegenValue> {



    private String codegenId;

    private String uniqueKey;

    private String vals;

    private int version;

    public String getCodegenId() {
        return codegenId;
    }

    public void setCodegenId(String codegenId) {
        this.codegenId = codegenId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getVals() {
        return vals;
    }

    public void setVals(String vals) {
        this.vals = vals;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}
