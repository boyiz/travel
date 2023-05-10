package com.xunye.notice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 通用注意事项DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-13
 */
@Data
@ColumnWidth(value = 25)
public class GeneralNoticeDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "费用不含")
    private String expenseExclude;

    @ExcelProperty(value = "费用包含")
    private String expenseInclude;

    @ExcelProperty(value = "退费规则")
    private String refundNotice;

    @ExcelProperty(value = "其他描述")
    private String description;

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
