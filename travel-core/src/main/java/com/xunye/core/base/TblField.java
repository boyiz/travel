package com.xunye.core.base;

import lombok.Data;

@Data
public class TblField {
    String dbFieldName;
    String objFieldName;
    int idx = 0;
}
