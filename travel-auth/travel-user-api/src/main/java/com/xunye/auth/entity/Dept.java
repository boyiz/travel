package com.xunye.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import com.xunye.core.em.StatusEnum;
import com.xunye.core.em.LogicDeleteEnum;
import lombok.Data;

/**
 * @ClassName Dept
 * @Description 部门实体
 * @Author boyiz
 * @Date 2023/4/19 15:18
 * @Version 1.0
 **/

@Data
@Entity
@Table(name = Dept.TABLE_NAME)
//@SQLDelete(sql = "update " + Dept.TABLE_NAME + " set is_delete = '1' where id = ?")
//@Where(clause = "is_delete = 0")
public class Dept extends BaseEntity {

    public static final String TABLE_NAME = "dept";

    /**
     * 部门名称
     */
    @Column(name = "dept_name", columnDefinition = "varchar(255) comment '部门名称'")
    private String deptName;
    /**
     * 负责人Ids
     */
    @Column(name = "leader_user_ids", columnDefinition = "varchar(255) comment '负责人Id'")
    private String leaderUserIds;
    /**
     * 负责人Names
     */
    @Column(name = "leader_user_names", columnDefinition = "varchar(255) comment '负责人名称'")
    private String leaderUserNames;
    /**
     * 状态
     */
    @Column(name = "dept_state", columnDefinition = "int comment '状态'")
    private Integer deptState = StatusEnum.Enable.getValue();
    /**
     * 父部门ID
     */
    @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父部门ID'")
    private String parentId;
    /**
     * 排序
     */
    @Column(name = "sort_no", columnDefinition = "int comment '排序'")
    private Integer sortNo;
    /**
     * 备注
     */
    @Column(name = "comments", columnDefinition = "varchar(255) comment '备注'")
    private String comments;
    /**
     * 逻辑删除
     */
    @Column(name = "is_delete", columnDefinition = "int comment '逻辑删除'")
    private Integer isDelete = LogicDeleteEnum.EXIST.getValue();

    /**
     * 是否为销售部门
     */
    @Column(name = "is_biz_dept", columnDefinition = "int comment '是否为销售部门'")
    private Integer isBizDept = 0;

}
