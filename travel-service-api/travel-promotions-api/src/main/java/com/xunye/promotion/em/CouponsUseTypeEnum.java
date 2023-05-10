package com.xunye.promotion.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName CouponsUseTypeEnum
 * @Description 优惠券使用类型
 * @Author boyiz
 * @Date 2023/4/23 15:44
 * @Version 1.0
 **/

@Getter
@AllArgsConstructor
public enum CouponsUseTypeEnum implements BaseEnum {

    ALL("全场通用", 0),
    CLASSIFY("指定分类", 1),
    GOODS("指定商品", 2),
    ;


    private final String label;
    private final Integer value;


}
