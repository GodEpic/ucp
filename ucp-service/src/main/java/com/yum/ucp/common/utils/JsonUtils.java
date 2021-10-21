package com.yum.ucp.common.utils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

/**
 * Json utils
 *
 * <p>Json utils base on com.fasterxml.jackson.databind.ObjectMapper. </p>
 *
 * @author hawods.pan on 2016/8/8 0008.
 * @updateby vincent.ding on 2018/06/06
 */
public class JsonUtils {

    public static final String DATE_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";

    private static ObjectMapper objectMapper = null;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT_LONG));
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    private JsonUtils() {
    }

    public static String stringify(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String stringify(Object object, String... properties) {
        try {
            return objectMapper
                    .writer(new SimpleFilterProvider().addFilter(
                            AnnotationUtils.getValue(
                                    AnnotationUtils.findAnnotation(object.getClass(), JsonFilter.class)).toString(),
                            SimpleBeanPropertyFilter.filterOutAllExcept(properties)))
                    .writeValueAsString(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void stringify(OutputStream out, Object object) {
        try {
            objectMapper.writeValue(out, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static <T> T parse(String json, Class<T> clazz) {
        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> List<T> parseList(String json, TypeReference typeReference) {
        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> Collection<T> parse(Collection sc, Collection<T> tc, Class<T> clazz){
        for(Object so : sc){
            T to = JsonUtils.parse(JsonUtils.stringify(so), clazz);
            tc.add(to);
        }
        return tc;
    }
}
