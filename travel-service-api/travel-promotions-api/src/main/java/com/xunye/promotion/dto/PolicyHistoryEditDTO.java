package com.xunye.promotion.dto;

import lombok.Data;
import java.util.Date;

/**
 * 优惠政策使用记录新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-05-02
 */
@Data
public class PolicyHistoryEditDTO {

    /* id */
    private String id;
    /* 活动id */
    private String policyId;
    /* 订单id */
    private String orderId;
    /* 订单号 */
    private String orderSn;
    /* 会员id */
    private String userId;
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

}
