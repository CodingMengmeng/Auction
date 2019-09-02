package com.example.auctionapp.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
public class BeanUtil<T> {


    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) {
        try {
            Map<String, Object> map = new HashMap<>();
            Class<?> clazz = obj.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = null;

                value = field.get(obj);

                map.put(fieldName, value);
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将任意class类转成Map对象
     * @param map
     * @param object
     * @param <T>
     * @return
     */
    public static<T> T mapToObject(Map<String, Object> map, T object) {
        try {

            Class<?> clazz = object.getClass();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();

                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    boolean equals = field.getName().equals(key);
                    if (equals) {
                        field.set(object, entry.getValue());
                    }

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

}
