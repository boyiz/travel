package com.xunye.promotion.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponsUseStatusEnum implements BaseEnum {

    UNUSED("未使用", 0),
    ALREADY_USE("已使用", 1),
    EXPIRED("已过期", 2),
    ;

    private final String label;
    private final Integer value;
}
