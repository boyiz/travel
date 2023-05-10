package com.xunye.promotion.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum CouponsGettingTypeEnum implements BaseEnum {

    SYSTEM_DONATE("后台赠送", 0),
    MANUAL_GET("主动获取", 1),
    ;

    private final String label;
    private final Integer value;
}
