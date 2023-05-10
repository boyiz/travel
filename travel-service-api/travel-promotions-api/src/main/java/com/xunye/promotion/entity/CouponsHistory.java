package com.xunye.promotion.entity;

import java.util.Date;

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
 * @ClassName CouponHistory
 * @Description 优惠券历史记录表
 * @Author boyiz
 * @Date 2023/4/23 15:35
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = CouponsHistory.TABLE_NAME)
@SQLDelete(sql = "UPDATE " + CouponsHistory.TABLE_NAME + " SET is_delete = '1' WHERE id = ?")
@Where(clause = BaseEntity.LOGIC_DELETE_CLAUSE_EXIST)
public class CouponsHistory extends BaseEntity {

    public static final String TABLE_NAME = "coupons_history";

    @Column(name = "coupon_id", columnDefinition = "varchar(32) comment '优惠券id'")
    private String couponId;

    @Column(name = "user_id", columnDefinition = "varchar(32) comment '会员id'")
    private String userId;

    @Column(name = "order_sn", columnDefinition = "varchar(32) comment '订单号码'")
    private String orderSn;
    @Column(name = "order_id", columnDefinition = "varchar(32) comment '订单id'")
    private String orderId;

    @Column(name = "coupon_code", columnDefinition = "varchar(32) comment '优惠券码'")
    private String couponCode;

    @Column(name = "getting_type", columnDefinition = " int(1) comment '获取类型：0->后台赠送；1->主动获取'")
    private Integer gettingType;

    @Column(name = "use_status", columnDefinition = " int(1) comment '使用状态：0->未使用；1->已使用；2->已过期'")
    private Integer useStatus;
    @Column(name = "use_time", columnDefinition = " datetime(3) comment '使用时间'")
    private Date useTime;

    @Column(name = "is_delete", columnDefinition = "int comment '逻辑删除'")
    private Integer isDelete = LogicDeleteEnum.EXIST.getValue();

}
