package com.xunye.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName OrderItem
 * @Description 订单详细信息
 * @Author boyiz
 * @Date 2023/4/24 15:20
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = OrderItem.TABLE_NAME)
public class OrderItem extends BaseEntity {

    public static final String TABLE_NAME = "order_item";

    @Column(name = "order_id", columnDefinition = "varchar(32) comment '订单id'")
    private String orderId;

    @Column(name = "order_sn", columnDefinition = "varchar(32) comment '订单编号'")
    private String orderSn;

    @Column(name = "route_id", columnDefinition = "varchar(32) comment '路线id'")
    private String routeId;

    @Column(name = "group_id", columnDefinition = "varchar(32) comment '路线开团id'")
    private String groupId;

    @Column(name = "quantity", columnDefinition = "varchar(32) comment '购买数量(人数)'")
    private String quantity;

    @Column(name = "roommate", columnDefinition = "varchar(32) comment '同行室友'")
    private String roommate;

    @Column(name = "policy_amount", columnDefinition = "varchar(32) comment '促销活动金额（促销价、满减、阶梯价）'")
    private String policyAmount;

    @Column(name = "coupon_amount", columnDefinition = "varchar(32) comment '优惠券抵扣金额'")
    private String couponAmount;

    @Column(name = "real_amount", columnDefinition = "varchar(32) comment '实际付款金额'")
    private String realAmount ;

    @Column(name = "description", columnDefinition = "text comment '描述'")
    private String description;

}
