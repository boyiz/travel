package com.xunye.promotion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName CouponProductRelation
 * @Description 优惠券-商品 关系表
 * @Author boyiz
 * @Date 2023/4/23 16:52
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = CouponsProductRelation.TABLE_NAME)
public class CouponsProductRelation extends BaseEntity {

    public static final String TABLE_NAME = "coupons_product_relation";

    @Column(name = "coupon_id", columnDefinition = "varchar(32) comment '优惠券id'")
    private String couponId;
    @Column(name = "group_id", columnDefinition = "varchar(32) comment '路线开团id'")
    private String groupId;

}
