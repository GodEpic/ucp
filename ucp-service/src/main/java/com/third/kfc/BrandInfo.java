package com.third.kfc;

import java.io.Serializable;

/**
 * Created by admin on 2019/3/22.
 */
public class BrandInfo implements Serializable {
    /**
     * 商户号
     */
    private String corpCode;
    /**
     * 品牌号
     * 肯德基：002
     * 必胜客：003
     * 东方既白：005
     * 小肥羊：006
     * TacoBell：007
     * C&J：008
     */
    private String brandCode;

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
}
