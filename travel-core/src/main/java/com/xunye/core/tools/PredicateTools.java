package com.xunye.core.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import org.slf4j.LoggerFactory;

public class PredicateTools {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PredicateTools.class);

    public static Predicate rebuildStringPredicate(Predicate predicate, String params, StringPath path, String method)
    {
        List<String> paramsArr = Arrays.asList(params.trim().split(","));
        for (int i=0;i<paramsArr.size(); i++) {
            paramsArr.set(i, paramsArr.get(i).trim());
        }
        if (paramsArr.size() > 1) {
            predicate = ExpressionUtils.and(predicate, path.in(paramsArr));
        }
        else {
            if (CheckTools.isNotNullOrEmpty(method) && method.equals("EQ")) {
                predicate = ExpressionUtils.and(predicate, path.eq(params.trim()));
            } else {
                predicate = ExpressionUtils.and(predicate, path.like("%" + params.trim() + "%"));
            }
        }
        return predicate;
    }

    public static Predicate rebuildNumberPredicate(Predicate predicate, String params, NumberPath path)
    {
        String[] paramsArr = params.split(",");
        LOGGER.info("{}", paramsArr.length);
        if (paramsArr.length <= 1) {
            predicate = ExpressionUtils.and(predicate, path.eq(Integer.valueOf(params)));
        }
        else {
            Predicate orPres = new BooleanBuilder();

            Integer iCount = 0;
            for (String param : paramsArr) {
                if (iCount == 0)
                    orPres = path.eq(Integer.valueOf(param));
                else
                    orPres = ExpressionUtils.or(orPres, path.eq(Integer.valueOf(param)));
                iCount++;
            }

            predicate = ExpressionUtils.and(predicate, orPres);
        }

        return predicate;
    }

    private void getFiledFromObject(QueryParam queryParam, Object object ) {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String objField = field.getName();

            if (!objField.equals(queryParam.getField())) {
                continue;
            }
            String objFieldUpper = objField.substring(0, 1).toUpperCase() + objField.substring(1);

            Method getMethod = null;
            Method setMethod = null;
            try {
                getMethod = object.getClass().getDeclaredMethod("get" + objFieldUpper);
                setMethod = object.getClass().getDeclaredMethod("set" + objFieldUpper);
            }
            catch(NoSuchMethodException e){
                System.out.println(e.toString());
                continue;
            }

            try {
                Object value = getMethod.invoke(object);
                System.out.println(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
    //public static Predicate buildPredicateByQueryPrm(Predicate predicate, List<QueryParam> queryParams, Object object, Map<String, Object> unknownFields ) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    //    Field[] fields = object.getClass().getDeclaredFields();
    //
    //    for(QueryParam queryParam:queryParams) {
    //        int dotIndex = queryParam.getField().indexOf(".");
    //        if (dotIndex >= 0) {
    //            String childObjName = queryParam.getField().substring(dotIndex + 1);
    //            for (Field field : fields) {
    //                if (field.getName().equals(childObjName)) {
    //
    //                }
    //            }
    //        }
    //        else {
    //
    //        }
    //        for (Field field : fields) {
    //            field.setAccessible(true);
    //            String objField = field.getName();
    //
    //            if (!objField.equals(queryParam.getField())) {
    //                continue;
    //            }
    //            String objFieldUpper = objField.substring(0, 1).toUpperCase() + objField.substring(1);
    //            Method getMethod = object.getClass().getDeclaredMethod("get" + objFieldUpper);
    //            Method setMethod = object.getClass().getDeclaredMethod("set" + objFieldUpper);
    //
    //            try {
    //                Object value = getMethod.invoke(object);
    //                System.out.println(value);
    //            } catch (IllegalAccessException e) {
    //                e.printStackTrace();
    //            } catch (InvocationTargetException e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }
    //
    //    return;
    //}
}
