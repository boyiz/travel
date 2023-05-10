package com.xunye.route.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 路线DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-12
 */
@Data
@ColumnWidth(value = 25)
public class RouteInfoDTO extends BaseDTO {

    @ExcelProperty(value = "路线ID")
    private String id;

    @ExcelProperty(value = "路线名称")
    private String routeName;

    @ExcelProperty(value = "路线标题")
    private String routeTitle;

    @ExcelProperty(value = "路线是否可见")
    private Integer visible;

    @ExcelProperty(value = "路线游玩天数")
    private Integer days;

    @ExcelProperty(value = "旅行项目位置区域")
    private String routeArea;

    @ExcelProperty(value = "路线时间区间")
    private String timeInterval;

    @ExcelProperty(value = "路线路径规划")
    private String routePlan;

    @ExcelProperty(value = "路径描述")
    private String description;

    @ExcelProperty(value = "路线始发地")
    private String originLocation;

    @ExcelProperty(value = "路线目的地")
    private String destinationLocation;

    @ExcelProperty(value = "路线图片")
    private String imgUrl;

    @ExcelProperty(value = "路线图片")
    private String maxImgUrl;

    @ExcelProperty(value = "路线图片")
    private String smallImgUrl;

    @ExcelProperty(value = "最大年龄")
    private Integer maxAge;

    @ExcelProperty(value = "最小年龄")
    private Integer minAge;

    @ExcelProperty(value = "最低价格")
    private Integer minPrice;

    @ExcelProperty(value = "最高价格")
    private Integer maxPrice;

    @ExcelProperty(value = "人数上限")
    private Integer maxUser;

    @ExcelProperty(value = "是否成团")
    private Integer hasGroup;

    @ExcelProperty(value = "关联文章介绍")
    private String wechatUrl;

    @ExcelProperty(value = "热门")
    private Integer hot;

    @ExcelProperty(value = "排序")
    private Integer sortRule;

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
