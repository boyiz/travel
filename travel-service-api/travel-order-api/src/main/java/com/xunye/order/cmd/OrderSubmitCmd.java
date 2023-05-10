package com.xunye.order.cmd;

import java.util.List;

import com.xunye.core.tools.CheckTools;
import com.xunye.order.vo.TripPerson;
import lombok.Data;
import org.springframework.util.Assert;

/**
 * @ClassName OrderSubmitVo
 * @Description 封装订单提交数据的vo
 * @Author boyiz
 * @Date 2023/4/29 15:38
 * @Version 1.0
 **/

@Data
public class OrderSubmitCmd {

    /* 路线id */
    private String routeId;
    /* 路线开团id */
    private String groupId;
    /* 购买数量(人数) */
    private Integer quantity;
    /** 支付方式 **/
    private Integer payType;
    /* 订单类型：0-&gt;正常订单；1-&gt;秒杀订单 */
    private Integer orderType;
    /* 同行室友 */
    private String roommate;


    /* 订单总金额 */
    private String totalAmount;
    /* 应付金额（实际支付金额） */
    private String payAmount;
    /* 促销活动金额（促销价、满减、阶梯价） */
    private String policyAmount;
    /* 优惠券抵扣金额 */
    private String couponAmount;

    //订单使用优惠券的list
    private List<String> couponsList;
    //订单使用活动信息的list
    private List<String> policyList;

    // 订单关联出行人
    private List<TripPerson> tripPersonList;

    private String remark;
    private String description;

    /** 防重令牌 **/
    private String orderToken;


    public void check() {
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routeId), "路线id为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(groupId), "开团id为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(quantity)||quantity == 0, "购买数量不正确");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(payType), "支付类型为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(orderType), "订单类型为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(totalAmount), "订单金额为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(payAmount), "支付金额为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(tripPersonList), "出行人为空");
        //Assert.isTrue(CheckTools.isNotNullOrEmpty(policyAmount), "促销活动金额为空");
        //Assert.isTrue(CheckTools.isNotNullOrEmpty(couponAmount), "优惠券抵扣金额为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(orderToken), "Token为空");


    }

}
