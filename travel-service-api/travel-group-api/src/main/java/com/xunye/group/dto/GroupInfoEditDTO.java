package com.xunye.group.dto;

import com.xunye.core.tools.CheckTools;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * group新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
@Data
public class GroupInfoEditDTO {

    /* id */
    private String id;
    /* 报名截止 */
    private Date deadline;
    /* 路线id */
    private String routeId;
    /* 开团时间 */
    private Date startDate;
    /* 团结束时间 */
    private Date endDate;
    /* 团名 */
    private String title;
    /* 团价格 */
    private String price;
    /* 团领队 */
    private String leader;
    /* 备注 */
    private String remark;
    /* 状态 */
    private Integer status;
    /* 最低开团人数 */
    private Integer minCount;
    /* 最多开团人数 */
    private Integer maxCount;
    /* 已报名人数 */
    private Integer actualCount;
    /*  */
    private Integer version;
    /* 距开团天数 */
    private Integer daysInterval;
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
        Assert.isTrue(CheckTools.isNotNullOrEmpty(routeId), "路线id为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(startDate), "开团时间为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(endDate), "结束时间为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(deadline), "报名截止时间为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(price), "价格为空");
    }

}
