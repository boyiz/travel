package com.xunye.promotion.dto;

import cn.hutool.core.lang.Assert;
import com.xunye.core.tools.CheckTools;
import lombok.Data;
import java.util.Date;

/**
 * 优惠券历史记录新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
@Data
public class CouponsHistoryEditDTO {

    /* id */
    private String id;
    /* 优惠券id */
    private String couponId;
    /* 会员id */
    private String userId;
    /* 订单号码 */
    private String orderSn;
    /* 订单id */
    private String orderId;
    /* 优惠券码 */
    private String couponCode;
    /* 获取类型：0-&gt;后台赠送；1-&gt;主动获取 */
    private Integer gettingType;
    /* 使用状态：0-&gt;未使用；1-&gt;已使用；2-&gt;已过期 */
    private Integer useStatus;
    /* 使用时间 */
    private Date useTime;
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
        Assert.isTrue(CheckTools.isNotNullOrEmpty(couponId), "关联优惠券ID为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(userId), "关联用户ID为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(gettingType), "获取类型为空");

    }

}
