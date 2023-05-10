package com.xunye.core.tools;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xunye.core.base.TblField;

public class ReflectUtils {

    /**
     * 通过字段名从对象或对象的父类中得到字段的值
     * @param object 对象实例
     * @param fieldName 字段名
     * @return 字段对应的值
     * @throws Exception
     */
    public static Object getValue(Object object, String fieldName) throws Exception {

        if (object == null) {

            return null;
        }
        if (StringUtils.isBlank(fieldName)) {

            return null;
        }
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {

            try {

                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {

                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }
        return null;
    }

    /**
     * 通过字段名从对象或对象的父类中得到字段的值（调用字典的get方法）
     * @param object 对象实例
     * @param fieldName 字段名
     * @return 字段对应的值
     * @throws Exception
     */
    public static Object getValueOfGet(Object object, String fieldName) throws Exception {

        if (object == null) {

            return null;
        }
        if (StringUtils.isBlank(fieldName)) {

            return null;
        }
        Field field = null;
        Class<?> clazz = object.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {

            try {

                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                //获得get方法
                Method getMethod = pd.getReadMethod();
                //执行get方法返回一个Object
                return getMethod.invoke(object);
            } catch (Exception e) {

                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            }
        }

        return null;
    }

    /**
     * 通过@Colume注解获取entity对象的字段名及表的字段名称
     */
    public static List<TblField> getTableFields(Class cls) {
        List<TblField> fieldDescList = new ArrayList<>();

        while (cls != null) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                Column property = field.getAnnotation(Column.class);
                if (property == null) {
                    continue;
                }
                String dbFieldName = property.name();
                if (dbFieldName != null && dbFieldName.length() > 0) {
                    TblField tblField = new TblField();
                    tblField.setDbFieldName(dbFieldName);
                    tblField.setObjFieldName(field.getName());
                    tblField.setIdx(tblField.getIdx() + 1);

                    fieldDescList.add(tblField);
                }
            }
            cls = cls.getSuperclass();
        }
        return fieldDescList;
    }

    /**
     * 通过@Table注解获取entity的表名
     */
    public static String getTableName(Class cls) {

        Table annotation = (Table) cls.getAnnotation(Table.class);

        return annotation==null?null:annotation.name();
    }

    /**
     * 通过字段名从对象或对象的父类中得到字段的值（调用字典的get方法，可以取出复杂的对象的值）
     * @param object 对象实例
     * @param fieldName 字段名
     * @return 字段对应的值
     * @throws Exception
     */
    public static Object getValueOfGetIncludeObjectFeild(Object object, String fieldName)
        throws Exception {
            if (object == null) {
                return null;
            }
            if (CheckTools.isNullOrEmpty(fieldName)) {
                return null;
            }
            if(HashMap.class.equals(object.getClass())){
                return ((Map)object).get(fieldName);
            }

            Field field = null;
            Class<?> clazz = object.getClass();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {

                try {

                    if (fieldName.contains(".")) {

                        // 如：operatorUser.name、operatorUser.org.name，递归调用
                        String[] splitFiledName = fieldName.split("\\.");
                        return getValueOfGetIncludeObjectFeild(
                                getValueOfGetIncludeObjectFeild(object, splitFiledName[0]),
                                splitFiledName[1]);
                    }
                    field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    PropertyDescriptor pd = new PropertyDescriptor(field.getName(), clazz);
                    //获得get方法
                    Method getMethod = pd.getReadMethod();
//                    System.out.printf("getMethod.getName():%s\n", getMethod.getName());                    System.out.printf("getMethod.getName():%s\n", getMethod.getName());
//                    System.out.printf("object:%s\n", object.toString());


                    //执行get方法返回一个Object
                    Object resultObj = getMethod.invoke(object);
//                    System.out.printf("resultObj:%s\n", resultObj.toString());
                    return resultObj;
                } catch (Exception e) {

                    //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                    //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
                }
            }
            return null;
    }

    /**
     * object转换为map
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?> cla = obj.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String keyName = field.getName();
            Object value = field.get(obj);
            if (value == null)
                value = "";
            map.put(keyName, value);
        }
        return map;
    }
}
