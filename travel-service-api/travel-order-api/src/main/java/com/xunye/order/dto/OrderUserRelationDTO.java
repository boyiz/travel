package com.xunye.order.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 用户订单关联用户DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
@ColumnWidth(value = 25)
public class OrderUserRelationDTO extends BaseDTO {

    @ExcelProperty(value = "")
    private String orderId;

    @ExcelProperty(value = "")
    private String orderSn;

    @ExcelProperty(value = "")
    private String routeId;

    @ExcelProperty(value = "")
    private String groupId;

    @ExcelProperty(value = "")
    private String userId;

    @ExcelProperty(value = "")
    private Integer userType;


}
