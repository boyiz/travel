package com.xunye.promotion.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName Policy
 * @Description 优惠政策实体
 * @Author boyiz
 * @Date 2023/4/22 16:47
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = PolicyInfo.TABLE_NAME)
public class PolicyInfo extends BaseEntity {

    public static final String TABLE_NAME = "policy_info";

    @Column(name = "route_id", columnDefinition = "varchar(32) comment '路线id'")
    private String routeId;

    @Column(name = "group_id", columnDefinition = "varchar(32) comment '路线开团id'")
    private String groupId;

    @Column(name = "price", columnDefinition = "varchar(32) comment '优惠金额'")
    private String price;

    @Column(name = "name", columnDefinition = "varchar(32) comment '优惠名称'")
    private String name;

    @Column(name = "effective_time", columnDefinition = "datetime(3)  comment '生效时间'")
    private Date effectiveTime;

    @Column(name = "failure_time", columnDefinition = "datetime(3)  comment '失效时间'")
    private Date failureTime;

    @Column(name = "status", columnDefinition = "int(3)  comment '启用状态'")
    private Integer status;

}
