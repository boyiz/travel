package com.xunye.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import com.xunye.core.em.LogicDeleteEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @ClassName Order
 * @Description 订单实体类
 * @Author boyiz
 * @Date 2023/4/24 12:58
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = OrderInfo.TABLE_NAME)
@SQLDelete(sql = "UPDATE " + OrderInfo.TABLE_NAME + " SET is_delete = '1' WHERE id = ?")
@Where(clause = BaseEntity.LOGIC_DELETE_CLAUSE_EXIST)
public class OrderInfo extends BaseEntity {

    public static final String TABLE_NAME = "order_info";

    @Column(name = "user_id", columnDefinition = "varchar(32) comment '会员id'")
    private String userId;

    @Column(name = "order_sn", columnDefinition = "varchar(32) comment '订单编号'")
    private String orderSn;

    @Column(name = "total_amount", columnDefinition = "varchar(32) comment '订单总金额'")
    private String totalAmount;

    @Column(name = "pay_amount", columnDefinition = "varchar(32) comment '应付金额（实际支付金额）'")
    private String payAmount;

    @Column(name = "policy_amount", columnDefinition = "varchar(32) comment '促销活动金额（促销价、满减、阶梯价）'")
    private String policyAmount;

    @Column(name = "coupon_amount", columnDefinition = "varchar(32) comment '优惠券抵扣金额'")
    private String couponAmount;

    @Column(name = "pay_type", columnDefinition = "int(1) comment '支付方式：0->未支付；1->支付宝；2->微信'")
    private Integer payType;

    @Column(name = "order_status", columnDefinition = "int(1) comment '0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单'")
    private Integer orderStatus;

    @Column(name = "order_type", columnDefinition = "int(1) comment '订单类型：0->正常订单；1->秒杀订单'")
    private Integer orderType;

    @Column(name = "remark", columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "is_delete", columnDefinition = "int comment '逻辑删除'")
    private Integer isDelete = LogicDeleteEnum.EXIST.getValue();


}
