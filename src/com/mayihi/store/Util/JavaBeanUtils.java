package com.mayihi.store.Util;

import com.mayihi.store.domain.User;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaBeanUtils {
    /**
     * 根据property属性名和返回值类型获取setter方法
     * @param property
     * @param javaType
     */
    public static String getGetterMethodName(String property, String javaType) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                //若第一个字符为小写，并且只有一个字符，或者第二个字符不为大写，则将第一个字符设为大写
//                username==》Username
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        //返回值类型，比如success（）的返回值类型为boolean
        if ("boolean".equals(javaType)) {
            //会在开头插入is，因此若是isSuccess==》isIsSuccess
            sb.insert(0, "is");
        } else {
            //否则，设置为getUsername
            sb.insert(0, "get");
        }

        return sb.toString();
    }

    /**
     * 根据property属性名获取setter方法
     * @param property
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    void test(Object... parameters) {

    }

    public static void main(String[] args) throws Exception {
//        给我一些属性名，我就可以调用getter，setter方法，将传给我的值赋给Object
        Map<String, String> properties = new HashMap<>();
        properties.put("username","zhangsan");
        properties.put("password","1234");
        Class clazz = User.class;
        Object o = clazz.newInstance();
        for (Map.Entry<String,String> entry: properties.entrySet()) {
            String setterMethodName = getSetterMethodName(entry.getKey());
            //使用反射得到对应的方法，并调用该方法
            Method method = clazz.getMethod(setterMethodName,String.class);
            method.invoke(o, entry.getValue());
        }
        System.out.println(o);

    }

}
