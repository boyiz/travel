package com.xunye.route.dto;

import cn.hutool.core.lang.Assert;
import com.xunye.core.tools.CheckTools;
import lombok.Data;
import java.util.Date;

/**
 * 路线新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-12
 */
@Data
public class RouteInfoEditDTO {

    /* 路线ID */
    private String id;
    /* 路线名称 */
    private String routeName;
    /* 路线标题 */
    private String routeTitle;
    /* 路线是否可见 */
    private Integer visible;
    /* 路线游玩天数 */
    private Integer days;
    /* 旅行项目位置区域 */
    private String routeArea;
    /* 路线时间区间 */
    private String timeInterval;
    /* 路线路径规划 */
    private String routePlan;
    /* 路径描述 */
    private String description;
    /* 路线始发地 */
    private String originLocation;
    /* 路线目的地 */
    private String destinationLocation;
    /* 路线图片 */
    private String imgUrl;
    /* 路线图片 */
    private String maxImgUrl;
    /* 路线图片 */
    private String smallImgUrl;
    /* 最大年龄 */
    private Integer maxAge;
    /* 最小年龄 */
    private Integer minAge;
    /* 最低价格 */
    private Integer minPrice;
    /* 最高价格 */
    private Integer maxPrice;
    /* 人数上限 */
    private Integer maxUser;
    /* 是否成团 */
    private Integer hasGroup;
    /* 关联文章介绍 */
    private String wechatUrl;
    /* 热门 */
    private Integer hot;
    /* 排序 */
    private Integer sortRule;
    /* 备注 */
    private String remark;
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
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routeName), "路线名称为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routeTitle), "路线标题为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routeName), "路线名称为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(days), "建议游玩天数为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routeArea), "路线位置区域为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(timeInterval), "路线建议游玩时间区间为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routePlan), "旅行路线规划为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(description), "路线描述为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(originLocation), "路线始发地为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(destinationLocation), "路线目的地为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(minPrice), "路线最低价格为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(maxPrice), "路线最高价格为空");
    }

}
