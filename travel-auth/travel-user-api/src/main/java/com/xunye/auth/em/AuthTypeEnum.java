package com.xunye.auth.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthTypeEnum implements BaseEnum {

    MENU("菜单", 0),
    PAGE("页面", 1),
    BTN("按钮", 2),
    ;

    private final String label;
    private final Integer value;


}
