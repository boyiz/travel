package com.xunye.order.dto;

import lombok.Data;
import java.util.Date;

/**
 * 用户订单关联用户新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
public class OrderUserRelationEditDTO {

    /*  */
    private String orderId;
    /*  */
    private String orderSn;
    /*  */
    private String routeId;
    /*  */
    private String groupId;
    /*  */
    private String userId;
    /*  */
    private Integer userType;

}
