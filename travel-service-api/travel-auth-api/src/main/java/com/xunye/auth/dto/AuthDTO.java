package com.xunye.auth.dto;

import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import com.xunye.core.base.BaseDTO;
import com.xunye.core.support.IHierarchical;
import lombok.Data;

/**
 * 权限DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Data
@ColumnWidth(value = 25)
public class AuthDTO extends BaseDTO implements IHierarchical<AuthDTO> {

    /**
     * 权限名称
     */
    @ExcelProperty(value = "权限名称")
    private String authName;
    /**
     * 权限标识
     */
    @ExcelProperty(value = "权限标识")
    private String authKey;
    /**
     * 权限类型
     */
    @ExcelIgnore
    private Integer authType;
    /**
     * 权限类型字面量
     */
    @ExcelProperty(value = "权限类型")
    private String authTypeStr;
    /**
     * 接口地址
     */
    @ExcelProperty(value = "接口地址")
    private String path;
    /**
     * 请求方式
     */
    @ExcelProperty(value = "请求方式")
    private String requestMethod;
    /**
     * 前端路由
     */
    @ExcelProperty(value = "前端路由")
    private String routePath;
    /**
     * 是否可见
     * 0 否
     * 1 是
     */
    @ExcelProperty(value = "是否可见")
    private Boolean isShow;
    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String icon;
    /**
     * 父权限ID
     */
    @ExcelProperty(value = "父权限ID")
    private String parentId;
    /**
     * 排序号
     */
    @ExcelProperty(value = "排序号")
    private Integer sortNo;
    /**
     * Antd配置项
     */
    @ExcelProperty(value = "Antd配置项")
    private String antdSettings;

    /**
     * 子节点
     */
    private List<AuthDTO> children;

    @Override
    public Integer fetchSortNo() {
        return sortNo;
    }
}
