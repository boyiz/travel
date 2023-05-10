package com.xunye.core.tools;

import java.util.List;

public class SQLTools {

    public static String in(List<?> inList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : inList) {
            stringBuilder.append(o);
            stringBuilder.append(",");
        }
        String target = stringBuilder.toString();
        return target.substring(0,target.length() - 1);
    }

}
