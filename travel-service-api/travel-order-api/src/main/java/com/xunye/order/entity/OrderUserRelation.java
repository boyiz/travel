package com.xunye.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseAutoIncreEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName OrderUserRelation
 * @Description 订单关联用户
 * @Author boyiz
 * @Date 2023/4/24 15:41
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = OrderUserRelation.TABLE_NAME)
public class OrderUserRelation extends BaseAutoIncreEntity {

    public static final String TABLE_NAME = "order_user_relation";

    @Column(name = "order_id", columnDefinition = "varchar(32) comment '订单id'")
    private String orderId;

    @Column(name = "order_sn", columnDefinition = "varchar(32) comment '订单编号'")
    private String orderSn;

    @Column(name = "route_id", columnDefinition = "varchar(32) comment '路线id'")
    private String routeId;

    @Column(name = "group_id", columnDefinition = "varchar(32) comment '路线开团id'")
    private String groupId;

    @Column(name = "user_id", columnDefinition = "varchar(32) comment '会员id'")
    private String userId;

    @Column(name = "user_type", columnDefinition = "int(1) comment '用户类型'")
    private Integer userType;
}
