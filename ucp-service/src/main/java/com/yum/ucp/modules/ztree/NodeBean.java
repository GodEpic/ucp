package com.yum.ucp.modules.ztree;

import java.io.Serializable;

/**
 * Ztree的节点对象
 *
 */
public class NodeBean implements Serializable
{
    private static final long serialVersionUID = 3954052430853957002L;

    private String id;// ID

    private String pId;// 父Id

    private String name;// 节点名称

    private Boolean open;// 是否打开

    private String brief;// 备注

    private Boolean nocheck;// 是否有checkbox

    private Boolean checked;// 是否被checked



    /**
     * 构建树的单个节点对象
     *
     * @param id 节点id，即节点值
     * @param pId 父节点id
     * @param name 节点描述
     * @param remark 节点详细说明
     * @param checked 节点是否被选中
     */
    public NodeBean(String id, String pId, String name, String remark, Boolean checked)
    {
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.brief = remark;
        if (checked == null)
        {
            checked = false;
        }
        else
        {
            this.checked = checked;
        }
        this.open = true;
        this.nocheck = false;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPId()
    {
        return pId;
    }

    public void setPId(String pId)
    {
        this.pId = pId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getOpen()
    {
        return open;
    }

    public void setOpen(Boolean open)
    {
        this.open = open;
    }

    public String getBrief()
    {
        return brief;
    }

    public void setBrief(String brief)
    {
        this.brief = brief;
    }

    public Boolean getNocheck()
    {
        return nocheck;
    }

    public void setNocheck(Boolean nocheck)
    {
        this.nocheck = nocheck;
    }

    public Boolean getChecked()
    {
        return checked;
    }

    public void setChecked(Boolean checked)
    {
        this.checked = checked;
    }


}