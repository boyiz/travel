package com.xunye.auth.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 角色权限实体DTO
 *
 * @Author: boyiz
 * @Date: 2023-05-08
 */
@Data
@ColumnWidth(value = 25)
public class RoleAuthDTO {

    @ExcelProperty(value = "id")
    private Long id;

    @ExcelProperty(value = "角色ID")
    private String roleId;

    @ExcelProperty(value = "权限ID")
    private String authId;


}
