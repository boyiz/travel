package com.xunye.promotion.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 优惠政策使用记录DTO
 *
 * @Author: boyiz
 * @Date: 2023-05-02
 */
@Data
@ColumnWidth(value = 25)
public class PolicyHistoryDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "活动id")
    private String policyId;

    @ExcelProperty(value = "订单id")
    private String orderId;

    @ExcelProperty(value = "订单号")
    private String orderSn;

    @ExcelProperty(value = "会员id")
    private String userId;

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
