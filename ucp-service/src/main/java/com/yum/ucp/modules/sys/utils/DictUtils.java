/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights
 * reserved.
 */
package com.yum.ucp.modules.sys.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yum.ucp.common.utils.CacheUtils;
import com.yum.ucp.common.utils.SpringContextHolder;
import com.yum.ucp.modules.sys.dao.DictDao;
import com.yum.ucp.modules.sys.entity.Dict;
import com.yum.ucp.modules.sys.entity.User;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字典工具类
 *
 * @author ThinkGem
 * @version 2013-5-29
 */
public class DictUtils {

    private static DictDao dictDao = SpringContextHolder.getBean(DictDao.class);
    private static transient Logger logger = Logger.getLogger(DictUtils.class.getName());
    public static final String CACHE_DICT_MAP = "dictMap";

    /*
     * public static String getDictLabel(String value, String type, String
     * defaultValue) { if (StringUtils.isNotBlank(type) &&
     * StringUtils.isNotBlank(value)) { for (Dict dict : getDictList(type)) { if
     * (type.equals(dict.getType()) && value.equals(dict.getValue())) { return
     * dict.getLabel(); } } } return defaultValue; }
     */

    public static String getDictLabels(String values, String type, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(values)) {
            List<String> valueList = Lists.newArrayList();
            for (String value : StringUtils.split(values, ",")) {
                valueList.add(getDictLabel(value, type, defaultValue));
            }
            return StringUtils.join(valueList, ",");
        }
        return defaultValue;
    }

    public static String getDictValue(String label, String type, String defaultLabel) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
            for (Dict dict : getDictList(type)) {
                if (type.equals(dict.getType()) && label.equals(dict.getLabel())) {
                    return dict.getValue();
                }
            }
        }
        return defaultLabel;
    }

    /*
     * public static List<Dict> getDictList(String type){
     *
     * @SuppressWarnings("unchecked") Map<String, List<Dict>> dictMap = (Map<String,
     * List<Dict>>)CacheUtils.get(CACHE_DICT_MAP); if (dictMap==null){ dictMap =
     * Maps.newHashMap(); for (Dict dict : dictDao.findAllList(new Dict())){
     * List<Dict> dictList = dictMap.get(dict.getType()); if (dictList != null){
     * dictList.add(dict); }else{ dictMap.put(dict.getType(),
     * Lists.newArrayList(dict)); } } CacheUtils.put(CACHE_DICT_MAP, dictMap); }
     * List<Dict> dictList = dictMap.get(type); if (dictList == null){ dictList =
     * Lists.newArrayList(); } return dictList; }
     */

    public static List<Dict> getDictList(String value) {
        // 先读取缓存中的字典数据，如果为空，则查询所有字典并加到缓存中
        @SuppressWarnings("unchecked")
        Map<String, List<Dict>> dictMap = (Map<String, List<Dict>>) CacheUtils.get(CACHE_DICT_MAP);
        if (dictMap == null) {
            dictMap = Maps.newHashMap();
            List<Dict> dictAllList = dictDao.findAllList(new Dict());
            for (Dict dict : dictAllList) {
                // 一级
                if ("0".equals(dict.getParentId())) {
                    List<Dict> dictList = getDictListByParentId(dict.getId(), dictAllList);
                    dictMap.put(dict.getValue(), dictList);
                }
            }
            CacheUtils.put(CACHE_DICT_MAP, dictMap);
        }
        List<Dict> dictList = dictMap.get(value);
        if (dictList == null) {
            dictList = Lists.newArrayList();
        } else {
            User user = UserUtils.getUser();
            if ("brand".equals(value) && null != user && StringUtils.isNotBlank(user.getBrand())) {
                List<Dict> list = new ArrayList<Dict>();
                for (Dict dic : dictList) {
                    if (dic.getValue().equals(user.getBrand())) {
                        list.add(dic);
                    }
                }
                dictList = list;
            }
        }
        return dictList;
    }

    private static List<Dict> getDictListByParentId(String parentId, List<Dict> list) {
        List<Dict> dictList = Lists.newArrayList();
        for (Dict dict : list) {
            if (parentId.equals(dict.getParentId())) {
                dictList.add(dict);
            }
        }
        return dictList;
    }

    public static String getDictLabel(String value, String type, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {

            for (Dict dict : getDictList(type)) {
                if (value.equals(dict.getValue())) {
                    return dict.getLabel();
                }
            }
        }
        return defaultValue;
    }

    public static Dict getByTypeAndValue(String type, String value) {
        Dict dict = dictDao.getByTypeAndValue(type, value);

        if (dict != null) {
            return dict;
        }
        return null;
    }

    public static Dict getByParentAndValue(Dict parentDict, String value) {
        List<Dict> lists = dictDao.findByParentId(parentDict.getId());
        if (lists != null && !lists.isEmpty()) {
            for (Dict dict : lists) {
                if (dict.getValue().equals(value)) {
                    return dict;
                }
            }
        }
        return null;
    }

    public static JSONArray formatDictList(String parentType) {
        List<Dict> lists = getDictList(parentType);
        JSONArray dictArray = new JSONArray();
        if (!lists.isEmpty()) {
            for (Dict dt : lists) {
                if (!dt.getParent().equals("0")) {
                    JSONObject object = new JSONObject();
                    object.put("value", dt.getValue());
                    object.put("label", dt.getLabel());

                    dictArray.add(object);
                }
            }
        }
        return dictArray;
    }
}
