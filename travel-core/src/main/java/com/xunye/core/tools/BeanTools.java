package com.xunye.core.tools;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class BeanTools extends BeanUtil {

    public static List<Map<String, Object>> toMapList(List<?> list) {
        return list.stream().map(BeanUtil::beanToMap).collect(Collectors.toList());
    }

    public static String[] getNullPropertyNames (Object source) {
//        org.springframework.beans.BeanWrapper
        final BeanWrapper src = new BeanWrapperImpl(source);
//        java.beans.PropertyDescriptor
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyNotNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }
}
