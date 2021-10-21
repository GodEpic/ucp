package com.yum.ucp.common.utils;

public enum BrandTypeEnum {
    //餐厅维护类型是1，V金维护类型是2
    RESTAURANT_BRAND("1"),VCODE_BRAND("2");

    private String name;

    /**
     *
     */
    public String getName()
    {
        return name;
    }

    BrandTypeEnum(final String name)
    {
        this.name = name;
    }
}
