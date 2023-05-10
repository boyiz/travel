package com.xunye.route.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.xunye.core.base.BaseAutoIncreEntity;
import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

/**
 * @ClassName Routes
 * @Description 路线实体
 * @Author boyiz
 * @Date 2023/4/12 14:25
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = RouteInfo.TABLE_NAME)
@SQLDelete(sql = "update " + RouteInfo.TABLE_NAME + " set is_delete = '1' where id = ?")
@Where(clause = "is_delete = 0")
public class RouteInfo extends BaseAutoIncreEntity {

    public static final String TABLE_NAME = "route_info";

    /**
     * 旅行项目名
     */
    @Column(name = "route_name", columnDefinition = "varchar(32) comment '路线名称'")
    private String routeName;

    /**
     * 旅行项目标题
     */
    @Column(name = "route_title", columnDefinition = "varchar(255) comment '路线标题'")
    private String routeTitle;

    /**
     * 旅行项目是否可见
     */
    @Column(name = "visible", columnDefinition = "int comment '路线是否可见'")
    private Integer visible = 0 ;

    /**
     * 旅行项目游玩天数
     */
    @Column(name = "route_days", columnDefinition = "int comment '路线游玩天数'")
    private Integer days;

    /**
     * 旅行项目位置区域
     */
    @Column(name = "route_area", columnDefinition = "varchar(32) comment '旅行项目位置区域'")
    private String routeArea;

    /**
     * 旅行项目时间区间
     */
    @Column(name = "time_interval", columnDefinition = "varchar(32) comment '路线时间区间'")
    private String timeInterval;

    /**
     * 旅行路径
     */
    @Column(name = "route_plan", columnDefinition = "text comment '路线路径规划'")
    private String routePlan;

    //路径描述
    @Column(name = "description", columnDefinition = "text comment '路径描述'")
    private String description;

    //路径始发地
    @Column(name = "origin_location", columnDefinition = "varchar(32) comment '路线始发地'")
    private String originLocation;

    //路径目的地
    @Column(name = "destination_location", columnDefinition = "varchar(32) comment '路线目的地'")
    private String destinationLocation;

    //路径图片
    @Column(name = "img_url", columnDefinition = "text comment '路线图片'")
    private String imgUrl;
    @Column(name = "max_img_url", columnDefinition = "text comment '路线图片'")
    private String maxImgUrl;
    @Column(name = "small_img_url", columnDefinition = "text comment '路线图片'")
    private String smallImgUrl;

    @Column(name = "max_age", columnDefinition = "int comment '最大年龄'")
    private Integer maxAge = 35;
    @Column(name = "min_age", columnDefinition = "int comment '最小年龄'")
    private Integer minAge = 18;

    @Column(name = "min_price", columnDefinition = "int comment '最低价格'")
    private Integer minPrice;
    @Column(name = "max_price", columnDefinition = "int comment '最高价格'")
    private Integer maxPrice;

    /**
     * 项目人数上限
     */
    @Column(name = "max_user", columnDefinition = "int comment '人数上限'")
    private Integer maxUser = 20;

    /**
     * 是否成团
     */
    @Column(name = "has_group", columnDefinition = "int comment '是否成团'")
    private Integer hasGroup = 0;

    @Column(name = "wechat_url", columnDefinition = "text comment '关联文章介绍'")
    private String wechatUrl;

    @Column(name = "hot", columnDefinition = "int comment '热门'")
    private Integer hot;

    @Column(name = "sort_rule", columnDefinition = "int comment '排序'")
    private Integer sortRule;

    @Column(name = "remark", columnDefinition = "text comment '备注'")
    private String remark;

    @Column(name = "is_delete", columnDefinition = "int(1) comment '是否删除'")
    private Integer isDelete = 0;
}