package com.xunye.core.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IdentityTypeEnum  implements BaseEnum {

    ID_CARD("身份证", 0),
    PASSPORT("护照", 1),
    HK_MACAO_RESIDENTS("回乡证", 2),
    TAIWAN_RESIDENTS("台胞证", 3),
    ;

    private final String label;
    private final Integer value;
}
