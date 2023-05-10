package com.xunye.core.tools;

import java.util.Collection;

public class CheckTools {

    /**
     * 判断对象是否为Null或空
     *
     * @param obj 需要判断的对象
     * @return True:空 False：非空
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (null == obj) {
            return true;
        }
        if (obj instanceof String) {
            if (((String) obj).isEmpty() || "null".equals(obj)) {
                return true;
            }
        }
        if (obj instanceof String[]) {
            if (((String[]) obj).length == 0) {
                return true;
            }
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        return false;
    }

    /**
     * 判断对象是否不为Null或非空
     *
     * @param obj 需要判断的对象
     * @return True:非空 False：空
     */
    public static boolean isNotNullOrEmpty(Object obj) {
        return !isNullOrEmpty(obj);
    }

    public static boolean ifChange(Object obj, Object obj2) {
        if (isNullOrEmpty(obj) && isNullOrEmpty(obj2)) {
            return false;
        }
        else if (isNullOrEmpty(obj) || isNullOrEmpty(obj2)){
            return true;
        }
        else {
            if (obj.equals(obj2)) {
                return false;
            }
            else {
                return true;
            }
        }
    }

    public static boolean isEqualIfStr(String obj1, String obj2) {
        if (isNullOrEmpty(obj1) && isNullOrEmpty(obj2)) {
            return true;
        } else if (isNullOrEmpty(obj1) || isNullOrEmpty(obj2)) {
            return false;
        } else {
            return obj1.equals(obj2);
        }
    }

    public static boolean isEqualIfInt(Integer obj1, Integer obj2) {
        Integer obj1Cp = obj1;
        if (isNullOrEmpty(obj1)) {
            obj1Cp = 0;
        }
        Integer obj2Cp = obj2;
        if (isNullOrEmpty(obj2)) {
            obj2Cp = 0;
        }
        return obj1Cp.equals(obj2Cp);

    }

}
