package com.yum.ucp.modules.sys.constants;

/**
 * Created by Administrator on 2016/10/17.
 */
public final class CommonConstants {
    public static String REFLECT_STRING = "java.lang.String";
    public static String REFLECT_DOUBLE = "java.lang.Double";
    public static String REFLECT_LIST = "java.util.List";

    /*
         *一二级分类是否选中
         */
    public static final class DICT_SELECT {
        public static final String YES = "1";//是
        public static final String NO = "0";//否
    }

    /*
       *1会员信息：2餐厅信息，3历史信息
       */
    public static final class HOT_LINE_CONFIG {
        public static final String MEM_INFO = "1";//是
        public static final String RES_INFO = "2";//否
        public static final String HISTORY_INFO = "3";//否
    }

    public static final class DscLinkFunctionType {
        public static final String THIRD = "0";//接口
        public static final String HREF = "1";//外链
        public static final String KONW = "2";//知识库
    }

    public static final class DscTaskLockType {
        //        0= 已锁定  /1= 被解锁
        public static final String YES = "0";
        public static final String NO = "1";
        public static final String LOCK = "处理中";
        public static final String UNLOCK = "待处理";
    }

    public static final class RoseType {
        public static final String LV1 = "1";
        public static final String LV2 = "2";
        public static final String LV3 = "3";
    }
//    0=初始/1=执行/9=成功/-1=异常
    public static final class ExportTask {
        public static final int INIT = 0;
        public static final int CARRY = 1;
        public static final int SUCCESS =9;
        public static final int ERROR =-1;
    }
}
