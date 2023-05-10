package com.xunye.auth.dto;

import java.util.ArrayList;
import java.util.List;

import com.xunye.core.em.LabelValue;
import lombok.Data;

/**
 * 部门新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Data
public class DeptEditDTO {

    /** ID */
    private String id;
    /** 部门名称 */
    private String deptName;

    // 负责人
    private String leaderUserIds;
    private String leaderUserNames;
    private List<LabelValue> leaderUserVoList = new ArrayList<>();

    /** 状态 */
    private Integer deptState;
    /** 父部门ID */
    private String parentId;
    /** 排序 */
    private Integer sortNo;
    /** 备注 */
    private String comments;
    /** 逻辑删除 */
    private Integer isDelete;
    /** 是否为销售部门 */
    private Integer isBizDept;

}
