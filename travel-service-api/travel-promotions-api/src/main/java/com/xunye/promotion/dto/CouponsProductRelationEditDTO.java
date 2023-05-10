package com.xunye.promotion.dto;

import lombok.Data;
import java.util.Date;

/**
 * 优惠券-商品 关系表新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
@Data
public class CouponsProductRelationEditDTO {

    /* id */
    private String id;
    /* 优惠券id */
    private String couponId;
    /* 路线开团id */
    private String groupId;
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
