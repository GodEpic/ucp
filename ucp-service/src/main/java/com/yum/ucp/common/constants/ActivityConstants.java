package com.yum.ucp.common.constants;

/**
 * @author tony
 * @version 2020/4/2
 */
public class ActivityConstants {
    public enum ActivityType {
        NORMAL("0", "普通活动"),
        URGENT("1", "紧急活动"),
        KEY_VALUE("2", "键值活动");

        private String type;
        private String desc;

        ActivityType(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
