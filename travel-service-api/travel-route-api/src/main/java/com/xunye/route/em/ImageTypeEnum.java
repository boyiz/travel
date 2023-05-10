package com.xunye.route.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageTypeEnum implements BaseEnum {

    COMMON("普通类型", 0),
    SLIDER("轮播类型", 1),
    ;

    private final String label;
    private final Integer value;
}
