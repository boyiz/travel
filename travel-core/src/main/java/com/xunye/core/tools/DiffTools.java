//package com.xunye.travel.core.tools;
//
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//import javax.persistence.Column;
//
//
//public class DiffTools {
//    public static Field[] getAllFields(Class clazz) {
//        List<Field> fieldList = new ArrayList<>();
//        while (clazz != null) {
//            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
//            clazz = clazz.getSuperclass();
//        }
//        Field[] fields = new Field[fieldList.size()];
//        fieldList.toArray(fields);
//        return fields;
//    }
//
//    public static Field[] getDeclaredFields(Class clazz) {
//        List<Field> fieldList = new ArrayList<>();
////        while (clazz != null) {
////            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
////            clazz = clazz.getSuperclass();
////        }
//        fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
//        Field[] fields = new Field[fieldList.size()];
//        fieldList.toArray(fields);
//        return fields;
//    }
//
//    public static String getDescriptionAnnotation(Field field) {
//        if (field.isAnnotationPresent(Column.class)) {
//            Column column = field.getAnnotation(Column.class);
//            String colDef = column.columnDefinition();
//            if (CheckTools.isNotNullOrEmpty(colDef)) {
//                int start = colDef.indexOf("comment ");
//                if (start != -1) {
//                    String commentOrigin = colDef.substring(start + "comment".length()).trim();
//                    commentOrigin = commentOrigin.replace("'", "");
//                    return commentOrigin;
//                }
//            }
//        }
//        return null;
//    }
//
//    public static boolean getDiffAnnotation(Field field) {
//        if (field.isAnnotationPresent(NxColumn.class)) {
//            NxColumn column = field.getAnnotation(NxColumn.class);
//            if (column != null)
//                return column.diffable();
//        }
//        return false;
//    }
//
//    public static List<Map<String, Object>> diffObj(Object oldObj, Object newObj, Class cls) {
//
//        boolean isEqual = true;
//        List<Map<String, Object>> resultList = new ArrayList<>();
//
//        /** 入参检查 */
//        if (oldObj == null && newObj == null) {
//            return resultList;
//        }
//        //clazz.getDeclaredFields()不能获取父的属性
//        Field[] fields = getDeclaredFields(cls);
//        for (Field field : fields) {
////            System.out.printf("class [%s], field:[%s]\n", cls.getName(), field.getName());
//            try {
//                if (field.getName().equals("createBy") || field.getName().equals("createByName")
//                        || field.getName().equals("createTime") || field.getName().equals("updateBy")
//                        || field.getName().equals("updateByName") || field.getName().equals("updateTime")
//                ) {
//                    continue;
//                }
//                boolean diffFlag = getDiffAnnotation(field);
//                if (!diffFlag) {
//                    continue;
//                }
//                if (oldObj == null && newObj != null) {
//                    field.setAccessible(true);
//                    Object oldValue = null;
//                    Object newValue = field.get(newObj);
//                    if (!Objects.equals(newValue, oldValue)) {
//                        Map<String, Object> diffResult = new HashMap<>();
//                        diffResult.put("field", field.getName());
//                        diffResult.put("fieldType", field.getClass().getSimpleName());
//                        diffResult.put("oldValue", oldValue);
//                        diffResult.put("newValue", newValue);
//                        String annotation = getDescriptionAnnotation(field);
//                        if (annotation != null) {
//                            diffResult.put("filedLabel", annotation);
//                        } else {
//                            // 如果取不到label，直接设置字段名
//                            diffResult.put("filedLabel", field.getName());
//                        }
//
//                        resultList.add(diffResult);
//                    }
//                } else if (oldObj != null && newObj != null) {
//                    field.setAccessible(true);
//                    Object oldValue = field.get(oldObj);
//                    Object newValue = field.get(newObj);
//                    if (!Objects.equals(newValue, oldValue)) {
//                        Map<String, Object> diffResult = new HashMap<>();
//                        diffResult.put("field", field.getName());
//                        diffResult.put("fieldType", field.getGenericType().getTypeName());
//                        diffResult.put("oldValue", oldValue);
//                        diffResult.put("newValue", newValue);
//                        String annotation = getDescriptionAnnotation(field);
//                        if (annotation != null) {
//                            diffResult.put("filedLabel", annotation);
//                        } else {
//                            // 如果取不到label，直接设置字段名
//                            diffResult.put("filedLabel", field.getName());
//                        }
//                        resultList.add(diffResult);
//                    }
//                }
//            } catch (Exception e) {
//                System.out.printf(e.getMessage());
//            }
//        }
//        return resultList;
//    }
//}
