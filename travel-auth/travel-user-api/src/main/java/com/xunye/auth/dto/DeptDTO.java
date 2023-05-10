package com.xunye.auth.dto;

import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xunye.core.base.BaseDTO;
import com.xunye.core.support.IHierarchical;
import lombok.Data;

/**
 * 部门DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Data
@ColumnWidth(value = 25)
public class DeptDTO extends BaseDTO implements IHierarchical<DeptDTO> {

    /**
     * ID
     */
    @ExcelProperty(value = "ID")
    private String id;
    /**
     * 部门名称
     */
    @ExcelProperty(value = "部门名称")
    private String deptName;

    // 负责人
    @ExcelIgnore
    private String leaderUserIds;
    @ExcelProperty(value = "负责人")
    private String leaderUserNames;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private Integer deptState;
    /**
     * 父部门ID
     */
    @ExcelProperty(value = "父部门ID")
    private String parentId;
    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sortNo;
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

    /**
     * 子部门
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<DeptDTO> children;

    @Override
    public Integer fetchSortNo() {
        return sortNo;
    }

    public String getKey() {
        return id;
    }

    @ExcelProperty(value = "是否为销售部门")
    private Integer isBizDept;

}
