package com.xunye.promotion.em;

import com.xunye.core.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName CouponsTypeEnum
 * @Description 优惠券类型
 * @Author boyiz
 * @Date 2023/4/22 21:06
 * @Version 1.0
 **/

@Getter
@AllArgsConstructor
public enum CouponsTypeEnum implements BaseEnum {

    ALL("全场赠券", 0),
    VIP("会员赠券", 1),
    SHOPPING("购物赠券", 2),
    REGISTER("注册赠券", 3),
    SYSTEM("系统下发", 4), // 通常为公司方过失产生，由管理员发放
    ;

    private final String label;
    private final Integer value;
}
