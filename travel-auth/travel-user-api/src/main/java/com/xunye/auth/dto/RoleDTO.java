package com.xunye.auth.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import com.xunye.core.base.BaseDTO;
import lombok.Data;

/**
 * 角色DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Data
@ColumnWidth(value = 25)
public class RoleDTO extends BaseDTO {

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private String id;
    /**
     * 角色名称
     */
    @ExcelProperty(value = "角色名称")
    private String roleName;
    /**
     * 角色标识
     */
    @ExcelProperty(value = "角色标识")
    private String roleKey;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Integer roleState;
    /**
     * 数据权限
     */
    @ExcelIgnore
    private String dataAuths;
    /**
     * 数据权限类型
     * 1. 仅自己(默认)
     * 2. 本部门
     * 3. 本部门及其子部门
     * 4. 自定义数据权限
     */
    @ExcelProperty(value = "数据权限")
    private Integer dataAuthType;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String comments;
    /**
     * 逻辑删除
     */
    @ExcelProperty(value = "逻辑删除")
    private Integer isDelete;

}
