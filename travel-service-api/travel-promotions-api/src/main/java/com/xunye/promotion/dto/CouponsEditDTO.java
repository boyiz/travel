package com.xunye.promotion.dto;

import cn.hutool.core.lang.Assert;
import com.xunye.core.tools.CheckTools;
import lombok.Data;
import java.util.Date;

/**
 * 优惠券新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
public class CouponsEditDTO {

    /* id */
    private String id;
    /* 名称 */
    private String name;
    /* 优惠券类型 */
    private Integer type;
    /* 使用平台 */
    private Integer platform;
    /* 数量 */
    private Long count;
    /* 金额 */
    private String amount;
    /* 每人限领张数 */
    private Integer perLimit;
    /* 使用门槛；0表示无门槛 */
    private String minPoint;
    /* 生效时间 */
    private Date effectiveTime;
    /* 失效时间 */
    private Date failureTime;
    /* 使用类型：0-&gt;全场通用；1-&gt;指定分类；2-&gt;指定商品 */
    private Integer useType;
    /* 备注 */
    private String remark;
    /* 发行数量 */
    private Long publishCount;
    /* 已使用数量 */
    private Long useCount;
    /* 领取数量 */
    private Long receiveCount;
    /* 可以领取的日期 */
    private Date enableTime;
    /* 优惠码 */
    private String code;
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
    /* 逻辑删除 */
    private Integer isDelete;

    public void check() {
        Assert.isTrue(CheckTools.isNotNullOrEmpty(name), "优惠券名称为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(type), "优惠券类型为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(count), "优惠券数量为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(amount), "优惠券金额为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(minPoint), "优惠券使用门槛为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(effectiveTime), "优惠券生效时间为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(failureTime), "优惠券失效时间为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(useType), "优惠券使用类型为空");
        //Assert.isTrue(CheckTools.isNotNullOrEmpty(publishCount), "优惠券发行数量为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(enableTime), "优惠券可领取时间为空");
    }
}
