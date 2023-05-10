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
 * @ClassName Coupons
 * @Description 优惠券实体
 * @Author boyiz
 * @Date 2023/4/22 16:35
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = Coupons.TABLE_NAME)
@SQLDelete(sql = "UPDATE " + Coupons.TABLE_NAME + " SET is_delete = '1' WHERE id = ?")
@Where(clause = BaseEntity.LOGIC_DELETE_CLAUSE_EXIST)
public class Coupons extends BaseEntity {

    public static final String TABLE_NAME = "coupons";

    @Column(name = "name", columnDefinition = "varchar(32) comment '名称'")
    private String name;

    @Column(name = "type", columnDefinition = "int(1) comment '优惠券类型'")
    private Integer type;

    @Column(name = "platform", columnDefinition = "int(1) comment '使用平台'")
    private Integer platform;

    @Column(name = "count", columnDefinition = "int comment '数量'")
    private Integer count;

    @Column(name = "amount", columnDefinition = "varchar(32) comment '金额'")
    private String amount;

    @Column(name = "per_limit", columnDefinition = "int(3) comment '每人限领张数'")
    private Integer perLimit;

    @Column(name = "min_point", columnDefinition = "varchar(16) comment '使用门槛；0表示无门槛'")
    private String minPoint;

    @Column(name = "effective_time", columnDefinition = "datetime(3)  comment '生效时间'")
    private Date effectiveTime;

    @Column(name = "failure_time", columnDefinition = "datetime(3)  comment '失效时间'")
    private Date failureTime;

    @Column(name = "use_type", columnDefinition = "int(1) comment '使用类型：0->全场通用；1->指定分类；2->指定商品'")
    private Integer useType;

    @Column(name = "remark", columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "publish_count", columnDefinition = "int comment '发行数量'")
    private Integer publishCount;

    @Column(name = "use_count", columnDefinition = "int comment '已使用数量'")
    private Integer useCount = 0;

    @Column(name = "receive_count", columnDefinition = "int comment '领取数量'")
    private Integer receiveCount;

    @Column(name = "enable_time", columnDefinition = "datetime(3) comment '可以领取的日期'")
    private Date enableTime;

    @Column(name = "code", columnDefinition = "varchar(32) comment '优惠码'")
    private String code;

    @Column(name = "is_delete", columnDefinition = "int comment '逻辑删除'")
    private Integer isDelete = LogicDeleteEnum.EXIST.getValue();

}
