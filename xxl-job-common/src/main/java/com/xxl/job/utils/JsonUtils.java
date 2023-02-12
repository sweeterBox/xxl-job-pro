package com.xxl.job.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static Object json2Obj(String jsonStr) {
        return json2Obj(jsonStr, Object.class);
    }

    public static <T> T  json2Obj(String jsonStr, Class<T> clazz) {
        if (jsonStr == null) {
            return null;
        }
        try {

            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    public static void writeJson2Obj2OutputStream(Object content, OutputStream outputStream) {
        if (Objects.isNull(content)) {
            return;
        }

        try {
            OBJECT_MAPPER.writeValue(outputStream, content);
        } catch (Exception e) {
            throw new RuntimeException("Json序列化出错", e);
        }


    }

    @SuppressWarnings({"rawtypes", "deprecation"})
    public static <T> T json2Obj(String content, Class<T> clazzItem, Class... classes) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(clazzItem, classes);
        try {
            return OBJECT_MAPPER.readValue(content, javaType);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化出错", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2Map(String content) {
        return json2Obj(content, Map.class);
    }

    public static <K, V> Map<K, V> json2Map(String content, Class<K> keyCls, Class<V> valueCls) {
        Map<String, Object> jsonMap = json2Map(content);
        Map<K, V> result = Maps.newHashMap();
        jsonMap.forEach((key, value) -> {
            K keyObj = json2Obj(key, keyCls);
            V valueObj = json2Obj(value.toString(), valueCls);
            result.put(keyObj, valueObj);
        });
        return result;
    }

    public static String obj2Json(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Json序列化出错", e);
        }
    }

}
