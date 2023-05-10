package com.xunye.order.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 订单详情DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
@ColumnWidth(value = 25)
public class OrderItemDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "订单id")
    private String orderId;

    @ExcelProperty(value = "订单编号")
    private String orderSn;

    @ExcelProperty(value = "路线id")
    private String routeId;

    @ExcelProperty(value = "路线开团id")
    private String groupId;

    @ExcelProperty(value = "购买数量(人数)")
    private String quantity;

    @ExcelProperty(value = "同行室友")
    private String roommate;

    @ExcelProperty(value = "促销活动金额（促销价、满减、阶梯价）")
    private String policyAmount;

    @ExcelProperty(value = "优惠券抵扣金额")
    private String couponAmount;

    @ExcelProperty(value = "实际付款金额")
    private String realAmount;

    @ExcelProperty(value = "描述")
    private String description;

    @ExcelProperty(value = "创建人ID")
    private String createBy;

    @ExcelProperty(value = "创建人员")
    private String createByName;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @ExcelProperty(value = "修改人ID")
    private String updateBy;

    @ExcelProperty(value = "修改人员")
    private String updateByName;

    @ExcelProperty(value = "修改时间")
    private Date updateTime;


}
