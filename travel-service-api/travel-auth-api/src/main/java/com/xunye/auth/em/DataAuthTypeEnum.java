package com.xunye.auth.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DataAuthTypeEnum implements BaseEnum {

    ONLY_ONESELF("仅自己", 1),
    THIS_DEPT("所在部门", 2),
    THIS_DEPT_AND_CHILDREN("所在部门及其子部门", 3),
    CUSTOMIZE("自定义数据权限范围", 4),
    ;

    private final String label;
    private final Integer value;


}