package com.xunye.order.dto;

import lombok.Data;
import java.util.Date;

/**
 * 订单表新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
public class OrderInfoEditDTO {

    /* id */
    private String id;
    /* 会员id */
    private String userId;
    /* 订单编号 */
    private String orderSn;
    /* 订单总金额 */
    private String totalAmount;
    /* 应付金额（实际支付金额） */
    private String payAmount;
    /* 促销活动金额（促销价、满减、阶梯价） */
    private String policyAmount;
    /* 优惠券抵扣金额 */
    private String couponAmount;
    /* 支付方式：0-&gt;未支付；1-&gt;支付宝；2-&gt;微信 */
    private Integer payType;
    /* 0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单 */
    private Integer orderStatus;
    /* 订单类型：0-&gt;正常订单；1-&gt;秒杀订单 */
    private Integer orderType;
    /* 备注 */
    private String remark;
    /* 逻辑删除 */
    private Long isDelete;
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
