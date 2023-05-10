package com.xunye.core.tools;

import java.util.Arrays;

import com.xunye.core.base.BaseEnum;

public class EnumTools {

    private static final String DEFAULT_NAME = "-";

    /**
     * 根据枚举值获取字面量
     *
     * @param enums 枚举选项
     * @param value 枚举值
     * @return 枚举字面量
     */
    public static String getNameByValue(BaseEnum[] enums, Integer value) {
        if (CheckTools.isNullOrEmpty(value)) {
            return DEFAULT_NAME;
        }
        return Arrays.stream(enums)
                .filter(em -> em.getValue().equals(value))
                .findFirst()
                .map(BaseEnum::getLabel)
                .orElse(DEFAULT_NAME);
    }

}
