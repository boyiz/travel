package com.xunye.promotion.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 优惠券历史记录DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-29
 */
@Data
@ColumnWidth(value = 25)
public class CouponsHistoryDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "优惠券id")
    private String couponId;

    @ExcelProperty(value = "会员id")
    private String userId;

    @ExcelProperty(value = "订单号码")
    private String orderSn;

    @ExcelProperty(value = "订单id")
    private String orderId;

    @ExcelProperty(value = "优惠券码")
    private String couponCode;

    @ExcelProperty(value = "获取类型：0-&gt;后台赠送；1-&gt;主动获取")
    private Integer gettingType;

    @ExcelProperty(value = "使用状态：0-&gt;未使用；1-&gt;已使用；2-&gt;已过期")
    private Integer useStatus;

    @ExcelProperty(value = "使用时间")
    private Date useTime;

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

    @ExcelProperty(value = "逻辑删除")
    private Integer isDelete;


}
