package com.xunye.core.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum StatusEnum implements BaseEnum {

    Enable("启用", 0),
    DISABLE("禁用", 1),
    ;

    private final String label;
    private final Integer value;

}
