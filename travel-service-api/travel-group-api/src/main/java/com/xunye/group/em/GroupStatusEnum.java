package com.xunye.group.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GroupStatusEnum implements BaseEnum {

    NOT_STARTED("未开团", 0),
    START("报名中", 1),
    END("结束", 2),
    ;
    private final String label;
    private final Integer value;
}
