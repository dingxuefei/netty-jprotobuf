package com.iscas.common.tools.reflect;

import com.iscas.common.tools.reflect.reflectTest.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 增强反射工具类测试
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/7/16
 * @since jdk1.8
 */
public class ReflectUtilsTests {
    private static A a = null;

    //初始化测试数据
    private static A11 getA11(){
        Random random = new Random();
        A11 a11x = new A11();
        a11x.setData(new float[]{3,4,5.6f,4.4545f,random.nextFloat()});
        Map map1 = new HashMap<>();
        map1.put("wge","ewg" + UUID.randomUUID());
        map1.put("weg",null);
        a11x.setMap(map1);
        a11x.setX1("weg");
        a11x.setX2(232.3346 + Math.random());
        a11x.setX4(34643.45777f);
        return a11x;
    }
    private static A2 getA2(){
        Random random = new Random();
        A2 a2 = new A2();
        a2.setX1(22 + random.nextInt(12));
        a2.setX2(235.2f);
        return a2;
    }
    static {
        a = new A();
        A1 a1 = new A1();
        a1.setA11List(Arrays.asList(getA11(),getA11(),getA11()));
        a.setA1(a1);
        a.setA2List(Arrays.asList(getA2(), getA2()));
        Map map1 = new HashMap();
        map1.put("qe",getA2());
        map1.put("wewg", getA2());
        a.setMap(map1);
    }

    /**
     * 通过反射获取某个对象的某个方法
     * */
    @Test
    public void test1() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int hash = (int) ReflectUtils.doMethod(a, "getA1Hash");
        System.out.println(hash);
    }

    /**
     * 通过反射获取某个对象的某个方法，并执行携带参数
     * */
    @Test
    public void test2() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String result = (String) ReflectUtils.doMethodWithParam(a, "xxx","x",34,new float[]{3,5,6,5.6f,56.78f});
        System.out.println(result);
    }

    /**判断一个类是不是基本数据类型*/
    @Test
    public void test3() throws NoSuchFieldException, IllegalAccessException {
        Integer a = new Integer(4);
        boolean flag = ReflectUtils.isWrapClass(a.getClass());
        Assert.assertEquals(flag, true);
        boolean flag1 = ReflectUtils.isWrapClass(A.class);
        Assert.assertEquals(flag1, false);
    }

    /**
     * 获取一个类和其父类的所有属性
     * */
    @Test
    public void test4(){
        List<Field> fields = ReflectUtils.findAllFieldsOfSelfAndSuperClass(A1111.class);
        System.out.println(fields);
    }

}
