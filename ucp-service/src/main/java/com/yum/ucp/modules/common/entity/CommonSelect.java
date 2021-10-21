package com.yum.ucp.modules.common.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class CommonSelect implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;
    private String label;
    private String isSelect;
    private String description;
    private List<CommonSelect> commonSelectList;

    public CommonSelect() {
    }

    public    CommonSelect(String value, String name, String isSelect) {
        this.name = name;
        this.value = value;
        this.isSelect = isSelect;
    }

    public    CommonSelect( String value,String name,String isSelect,String description) {
        this.name = name;
        this.value = value;
        this.isSelect = isSelect;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }

    public String getName() {
        return name;
    }

    public List<CommonSelect> getCommonSelectList() {
        return commonSelectList;
    }

    public void setCommonSelectList(List<CommonSelect> commonSelectList) {
        this.commonSelectList = commonSelectList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
