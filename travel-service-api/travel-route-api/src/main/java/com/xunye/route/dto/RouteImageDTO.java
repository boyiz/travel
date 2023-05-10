package com.xunye.route.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 路线介绍图DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-18
 */
@Data
@ColumnWidth(value = 25)
public class RouteImageDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "关联路线id")
    private String routeId;

    @ExcelProperty(value = "行程的第几天")
    private Integer dayNo;

    @ExcelProperty(value = "图片路径")
    private String imageUrl;

    @ExcelProperty(value = "图片类型")
    private Integer imageType;

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
