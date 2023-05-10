package com.xunye.order.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 订单表DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
@ColumnWidth(value = 25)
public class OrderInfoDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "会员id")
    private String userId;

    @ExcelProperty(value = "订单编号")
    private String orderSn;

    @ExcelProperty(value = "订单总金额")
    private String totalAmount;

    @ExcelProperty(value = "应付金额（实际支付金额）")
    private String payAmount;

    @ExcelProperty(value = "促销活动金额（促销价、满减、阶梯价）")
    private String policyAmount;

    @ExcelProperty(value = "优惠券抵扣金额")
    private String couponAmount;

    @ExcelProperty(value = "支付方式：0-&gt;未支付；1-&gt;支付宝；2-&gt;微信")
    private Integer payType;

    @ExcelProperty(value = "0待付款；1待发货；2已发货；3已完成；4已关闭；5无效订单")
    private Integer orderStatus;

    @ExcelProperty(value = "订单类型：0-&gt;正常订单；1-&gt;秒杀订单")
    private Integer orderType;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "逻辑删除")
    private Long isDelete;

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
