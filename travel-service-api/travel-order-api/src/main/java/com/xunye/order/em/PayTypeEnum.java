package com.xunye.order.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayTypeEnum implements BaseEnum {

    UNPAID("未支付", 0),
    ALIPAY("支付宝", 1),
    WECHAT("微信支付", 2),
    ;

    private final String label;
    private final Integer value;
}