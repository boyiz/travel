package com.xunye.order.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum  implements BaseEnum {

    WAIT_PAY("待付款", 0),
    WAIT_DELIVER("待发货", 1),
    ALREADY_DELIVER("已发货", 2),
    FINISH("已完成", 3),
    CLOSED("已关闭", 4),
    INVALID("无效订单", 5),
    ;

    private final String label;
    private final Integer value;
}
