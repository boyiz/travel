package com.xunye.route.dto;

import cn.hutool.core.lang.Assert;
import com.xunye.core.tools.CheckTools;
import lombok.Data;
import java.util.Date;

/**
 * 路线-每天计划新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-15
 */
@Data
public class RouteScheduleEditDTO {

    /* id */
    private String id;
    /* 关联路线id */
    private String routeId;
    /* 行程的第几天 */
    private Integer dayNo;
    /* 标题 */
    private String title;
    /* 当天计划详细规划 */
    private String detail;
    /* 当天计划简介 */
    private String introduction;
    /* 当天计划安排 */
    private String plan;
    /* 美食 */
    private String food;
    /* 入住酒店 */
    private String hotel;
    /* 服装 */
    private String clothing;
    /* 景点 */
    private String landscape;
    /* 游玩 */
    private String play;
    /* 其他注意事项 */
    private String notice;
    /* 创建人ID */
    private String createBy;
    /* 创建人员 */
    private String createByName;
    /* 创建时间 */
    private Date createTime;
    /* 修改人ID */
    private String updateBy;
    /* 修改人员 */
    private String updateByName;
    /* 修改时间 */
    private Date updateTime;

    public void check() {
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routeId), "关联路线id为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(dayNo), "关联行程天数为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(detail), "当天详细规划为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(plan), "当天详细计划为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(food), "当天食物计划为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(hotel), "当天酒店计划为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(clothing), "当天服装计划为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(landscape), "当天景点计划为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(play), "当天游玩计划为空");
    }

}
