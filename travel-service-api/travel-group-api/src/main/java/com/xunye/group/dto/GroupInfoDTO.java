package com.xunye.group.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * groupDTO
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
@Data
@ColumnWidth(value = 25)
public class GroupInfoDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "路线id")
    private String routeId;

    @ExcelProperty(value = "报名截止")
    private Date deadline;

    @ExcelProperty(value = "开团时间")
    private Date startDate;

    @ExcelProperty(value = "团结束时间")
    private Date endDate;

    @ExcelProperty(value = "团名")
    private String title;

    @ExcelProperty(value = "团价格")
    private String price;

    @ExcelProperty(value = "团领队")
    private String leader;

    @ExcelProperty(value = "备注")
    private String remark;

    @ExcelProperty(value = "状态")
    private Integer status;

    @ExcelProperty(value = "最低开团人数")
    private Integer minCount;

    @ExcelProperty(value = "最多开团人数")
    private Integer maxCount;

    @ExcelProperty(value = "已报名人数")
    private Integer actualCount;

    @ExcelProperty(value = "")
    private Integer version;

    @ExcelProperty(value = "距开团天数")
    private Integer daysInterval;

    @JsonIgnore
    @ExcelProperty(value = "创建人ID")
    private String createBy;

    @JsonIgnore
    @ExcelProperty(value = "创建人员")
    private String createByName;

    @JsonIgnore
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @JsonIgnore
    @ExcelProperty(value = "修改人ID")
    private String updateBy;

    @JsonIgnore
    @ExcelProperty(value = "修改人员")
    private String updateByName;

    @JsonIgnore
    @ExcelProperty(value = "修改时间")
    private Date updateTime;


}
