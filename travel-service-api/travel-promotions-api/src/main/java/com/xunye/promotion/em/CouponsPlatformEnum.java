package com.xunye.promotion.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponsPlatformEnum implements BaseEnum {

    ALL("全平台", 0),
    MOBILE("移动端", 1),
    PC("PC端", 2),
    ;

    private final String label;
    private final Integer value;
}
