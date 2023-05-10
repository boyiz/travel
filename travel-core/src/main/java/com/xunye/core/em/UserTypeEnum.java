package com.xunye.core.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum implements BaseEnum {

    NORMAL_USER("普通用户", 0),
    REGULAR_ADMIN("普通管理员", 1),
    SUPER_ADMIN("超级管理员", 2),
    ;

    private final String label;
    private final Integer value;
}
