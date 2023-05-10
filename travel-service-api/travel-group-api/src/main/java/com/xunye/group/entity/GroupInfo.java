package com.xunye.group.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.xunye.core.base.BaseAutoIncreEntity;
import com.xunye.core.base.BaseEntity;
import com.xunye.group.em.GroupStatusEnum;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName GroupInfo
 * @Description 路线开团信息
 * @Author boyiz
 * @Date 2023/4/13 14:50
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = GroupInfo.TABLE_NAME)
public class GroupInfo extends BaseAutoIncreEntity {

    public static final String TABLE_NAME = "group_info";

    @Column(name = "route_id", columnDefinition = "varchar(32) comment '路线id'")
    private String routeId;

    @Column(name = "deadline", columnDefinition = "datetime(3) comment '报名截止'")
    private Date deadline;

    @Column(name = "start_date", columnDefinition = "datetime(3) comment '开团时间'")
    private Date startDate;

    @Column(name = "end_date", columnDefinition = "datetime(3) comment '团结束时间'")
    private Date endDate;

    @Column(name = "title", columnDefinition = "varchar(32) comment '团名'")
    private String title;

    @Column(name = "price", columnDefinition = "varchar(32) comment '团价格'")
    private String price;

    @Column(name = "leader", columnDefinition = "varchar(32) comment '团领队'")
    private String leader;

    @Column(name = "remark", columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "status", columnDefinition = "int comment '状态'")
    private Integer status = GroupStatusEnum.NOT_STARTED.getValue();

    @Column(name = "min_count", columnDefinition = "int comment '最低开团人数'")
    private Integer minCount = 5;

    @Column(name = "max_count", columnDefinition = "text comment '最多开团人数'")
    private Integer maxCount = 20;

    @Column(name = "actual_count", columnDefinition = "text comment '已报名人数'")
    private Integer actualCount = 0;

    @Column(name = "version", columnDefinition = "int comment ''")
    private Integer version;

    @Column(name = "days_interval", columnDefinition = "int comment '距开团天数'")
    private Integer daysInterval;

}
