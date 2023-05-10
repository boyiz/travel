package com.xunye.notice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 路线注意事项DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-14
 */
@Data
@ColumnWidth(value = 25)
public class RouteNoticeDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "关联路线id")
    private String routeId;

    @ExcelProperty(value = "准备事宜")
    private String preparations;

    @ExcelProperty(value = "关于当地")
    private String aboutLocal;

    @ExcelProperty(value = "关于交通")
    private String aboutTraffic;

    @ExcelProperty(value = "其他事宜")
    private String otherIssues;

    @ExcelProperty(value = "备注")
    private String remark;

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
