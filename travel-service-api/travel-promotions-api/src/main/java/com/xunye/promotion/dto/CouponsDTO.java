package com.xunye.promotion.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 优惠券DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-28
 */
@Data
@ColumnWidth(value = 25)
public class CouponsDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "优惠券类型")
    private Integer type;

    @ExcelProperty(value = "使用平台")
    private Integer platform;

    @ExcelProperty(value = "数量")
    private Long count;

    @ExcelProperty(value = "金额")
    private String amount;

    @ExcelProperty(value = "每人限领张数")
    private Integer perLimit;

    @ExcelProperty(value = "使用门槛；0表示无门槛")
    private String minPoint;

    @ExcelProperty(value = "生效时间")
    private Date effectiveTime;

    @ExcelProperty(value = "失效时间")
    private Date failureTime;

    @ExcelProperty(value = "使用类型：0-&gt;全场通用；1-&gt;指定分类；2-&gt;指定商品")
    private Integer useType;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "发行数量")
    private Long publishCount;

    @ExcelProperty(value = "已使用数量")
    private Long useCount;

    @ExcelProperty(value = "领取数量")
    private Long receiveCount;

    @ExcelProperty(value = "可以领取的日期")
    private Date enableTime;

    @ExcelProperty(value = "优惠码")
    private String code;

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
