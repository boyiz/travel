package com.xunye.core.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogicDeleteEnum implements BaseEnum {


    EXIST("存在", 0),
    DELETE("删除", 1),
    ;

    private final String label;
    private final Integer value;
    
}
