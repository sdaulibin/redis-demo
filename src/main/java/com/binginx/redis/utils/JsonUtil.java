package com.binginx.redis.utils;

import com.alibaba.fastjson.JSON;

public class JsonUtil {
    /**
     * 将对象转换成json字符串
     * @param object
     * @return
     */
    public static String toJSONString(Object object){
        if(object!=null){
            return JSON.toJSONString(object);
        }
        return null;
    }

    /**
     * 将json字符串转成对象T
     * @param <T>
     * @param jsonStr
     * @return
     */
    public static <T> Object parseObject(String jsonStr, Class<T> c){
        if(jsonStr!=null){
            return JSON.parseObject(jsonStr, c);
        }
        return null;
    }
}
