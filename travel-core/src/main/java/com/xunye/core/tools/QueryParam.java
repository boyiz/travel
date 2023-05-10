package com.xunye.core.tools;

import lombok.Data;

/**
 *
 * @Author: gen
 * @Date: 2021-08-10
 */
@Data
public class QueryParam {
    private String field;
    private String method;
    private String values;
    private String sorterOrder;
}
