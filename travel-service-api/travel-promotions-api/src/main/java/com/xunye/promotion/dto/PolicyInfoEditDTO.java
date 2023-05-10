package com.xunye.promotion.dto;

import lombok.Data;
import java.util.Date;

/**
 * 优惠政策实体新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-22
 */
@Data
public class PolicyInfoEditDTO {

    /* id */
    private String id;
    /* 路线id */
    private String routeId;
    /* 路线开团id */
    private String groupId;
    /* 优惠金额 */
    private String price;
    /* 优惠名称 */
    private String name;
    /* 生效时间 */
    private Date effectiveTime;
    /* 失效时间 */
    private Date failureTime;
    /* 启用状态 */
    private Integer status;
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
