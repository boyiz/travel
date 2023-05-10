package com.xunye.route.dto;

import com.xunye.core.tools.CheckTools;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 路线介绍图新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-18
 */
@Data
public class RouteImageEditDTO {

    /* id */
    private String id;
    /* 关联路线id */
    private String routeId;
    /* 行程的第几天 */
    private Integer dayNo;
    /* 图片路径 */
    private String imageUrl;
    /* 图片类型 */
    private Integer imageType;
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
        Assert.isTrue(CheckTools.isNotNullOrEmpty(imageUrl), "图片路径为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(imageType), "图片类型为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(dayNo), "图片对应行程天数为空");
    }
}
