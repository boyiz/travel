package com.xunye.route.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 路线-每天计划DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-15
 */
@Data
@ColumnWidth(value = 25)
public class RouteScheduleDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "关联路线id")
    private String routeId;

    @ExcelProperty(value = "行程的第几天")
    private Integer dayNo;

    @ExcelProperty(value = "标题")
    private String title;

    @ExcelProperty(value = "当天计划详细规划")
    private String detail;

    @ExcelProperty(value = "当天计划简介")
    private String introduction;

    @ExcelProperty(value = "当天计划安排")
    private String plan;

    @ExcelProperty(value = "美食")
    private String food;

    @ExcelProperty(value = "入住酒店")
    private String hotel;

    @ExcelProperty(value = "服装")
    private String clothing;

    @ExcelProperty(value = "景点")
    private String landscape;

    @ExcelProperty(value = "游玩")
    private String play;

    @ExcelProperty(value = "其他注意事项")
    private String notice;

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
