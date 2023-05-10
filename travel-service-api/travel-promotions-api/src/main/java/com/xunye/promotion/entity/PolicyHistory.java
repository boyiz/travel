package com.xunye.promotion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @ClassName PolicyHistory
 * @Description 订单优惠信息使用记录
 * @Author boyiz
 * @Date 2023/4/30 11:04
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = PolicyHistory.TABLE_NAME)
//@SQLDelete(sql = "UPDATE " + PolicyHistory.TABLE_NAME + " SET is_delete = '1' WHERE id = ?")
//@Where(clause = BaseEntity.LOGIC_DELETE_CLAUSE_EXIST)
public class PolicyHistory extends BaseEntity {

    public static final String TABLE_NAME = "policy_history";

    @Column(name = "policy_id", columnDefinition = "varchar(32) comment '活动id'")
    private String policyId;
    @Column(name = "order_id", columnDefinition = "varchar(32) comment '订单id'")
    private String orderId;
    @Column(name = "order_sn", columnDefinition = "varchar(32) comment '订单号'")
    private String orderSn;
    @Column(name = "user_id", columnDefinition = "varchar(32) comment '会员id'")
    private String userId;
}
