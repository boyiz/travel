package com.xunye.order.dto;

import lombok.Data;
import java.util.Date;

/**
 * 订单详情新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
public class OrderItemEditDTO {

    /* id */
    private String id;
    /* 订单id */
    private String orderId;
    /* 订单编号 */
    private String orderSn;
    /* 路线id */
    private String routeId;
    /* 路线开团id */
    private String groupId;
    /* 购买数量(人数) */
    private String quantity;
    /* 同行室友 */
    private String roommate;
    /* 促销活动金额（促销价、满减、阶梯价） */
    private String policyAmount;
    /* 优惠券抵扣金额 */
    private String couponAmount;
    /* 实际付款金额 */
    private String realAmount;
    /* 描述 */
    private String description;
    /* 创建人ID */
    private String createBy;
    /* 创建人员 */
    private String createByName;
    /* 创建时间 */
    private Date createTime;
    /* 修改人ID */
    private String updateBy;
    /* 修改人员 */
    private String updateByName;
    /* 修改时间 */
    private Date updateTime;

}
