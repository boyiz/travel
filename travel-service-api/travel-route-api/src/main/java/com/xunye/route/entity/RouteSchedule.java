package com.xunye.route.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseAutoIncreEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @ClassName RouteSchedule
 * @Description 路线行程安排
 * @Author boyiz
 * @Date 2023/4/14 18:31
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = RouteSchedule.TABLE_NAME)
public class RouteSchedule extends BaseAutoIncreEntity {

    public static final String TABLE_NAME = "route_schedule";

    @Column(name = "route_id", columnDefinition = "varchar(32) comment '关联路线id'")
    private String routeId;
    @Column(name = "day_no", columnDefinition = "int comment '行程的第几天'")
    private Integer dayNo;
    @Column(name = "title", columnDefinition = "text comment '标题'")
    private String title;
    @Column(name = "detail", columnDefinition = "text comment '当天计划详细规划'")
    private String detail;
    // 当天简介
    @Column(name = "introduction", columnDefinition = "text comment '当天计划简介'")
    private String introduction;
    // 当天行程安排计划
    @Column(name = "plan", columnDefinition = "text comment '当天计划安排'")
    private String plan;
    // 美食
    @Column(name = "food", columnDefinition = "text comment '美食'")
    private String food;
    // 酒店
    @Column(name = "hotel", columnDefinition = "text comment '入住酒店'")
    private String hotel;
    // 服装
    @Column(name = "clothing", columnDefinition = "text comment '服装'")
    private String clothing;
    // 景点
    @Column(name = "landscape", columnDefinition = "text comment '景点'")
    private String landscape;
    // 游玩
    @Column(name = "play", columnDefinition = "text comment '游玩'")
    private String play;
    // 注意
    @Column(name = "notice", columnDefinition = "text comment '其他注意事项'")
    private String notice;

}
