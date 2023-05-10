package com.xunye.core.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiffResult {

    /**
     * 差异的结果属性
     */
    private String propertyName;
    /**
     * 原来的属性
     */
    private Object originalValue;


    /**
     *  当前的属性
     */
    private Object currentValue;
}