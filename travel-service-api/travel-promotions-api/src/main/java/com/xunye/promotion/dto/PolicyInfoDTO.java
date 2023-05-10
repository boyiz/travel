package com.xunye.promotion.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 优惠政策实体DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-22
 */
@Data
@ColumnWidth(value = 25)
public class PolicyInfoDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "路线id")
    private String routeId;

    @ExcelProperty(value = "路线开团id")
    private String groupId;

    @ExcelProperty(value = "优惠金额")
    private String price;

    @ExcelProperty(value = "优惠名称")
    private String name;

    @ExcelProperty(value = "生效时间")
    private Date effectiveTime;

    @ExcelProperty(value = "失效时间")
    private Date failureTime;

    @ExcelProperty(value = "启用状态")
    private Integer status;

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
