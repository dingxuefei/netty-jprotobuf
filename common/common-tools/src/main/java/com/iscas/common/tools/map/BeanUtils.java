package com.iscas.common.tools.map;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {
    /**
     * 实体类非空属性转map
     * @param bean 实体类
     */
    public static <T> Map beanToMap(T bean) {
        Map map = new HashMap();
        Class clazz = bean.getClass();
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
            setMap(field,bean,map);
        });
        return map;
    }
    /**
     * 获取字段的值map
     * @param field
     */
    public static <T> void setMap(Field field, T bean, Map map) {
        //访问private限制
        field.setAccessible(true);
        try {
            Object fieldValue = field.get(bean);
            if (fieldValue != null) {
                map.put(field.getName(), fieldValue);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
