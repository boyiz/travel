package com.xunye.order.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderTypeEnum implements BaseEnum {

    NORMAL("正常订单", 0),
    SECKILL("秒杀订单", 1),
    ;

    private final String label;
    private final Integer value;
}
