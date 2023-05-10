package com.xunye.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import com.xunye.core.em.LogicDeleteEnum;
import com.xunye.auth.em.DataAuthTypeEnum;
import com.xunye.core.em.StatusEnum;
import lombok.Data;

/**
 * @ClassName Role
 * @Description 角色实体
 * @Author boyiz
 * @Date 2023/4/19 15:18
 * @Version 1.0
 **/

@Data
@Entity
@Table(name = Role.TABLE_NAME)
public class Role extends BaseEntity {

    public static final String TABLE_NAME = "role";

    /**
     * 角色名称
     */
    @Column(name = "role_name", columnDefinition = "varchar(255) comment '角色名称'")
    private String roleName;
    /**
     * 角色标识
     */
    @Column(name = "role_key", columnDefinition = "varchar(100) comment '角色标识'")
    private String roleKey;
    /**
     * 状态
     */
    @Column(name = "role_state", columnDefinition = "int comment '状态'")
    private Integer roleState = StatusEnum.Enable.getValue();

    /**
     * 数据权限类型
     * 1. 仅自己(默认)
     * 2. 本部门
     * 3. 本部门及其子部门
     * 4. 自定义数据权限
     */
    @Column(name = "data_auth_type", columnDefinition = "int comment '数据权限类型'")
    private Integer dataAuthType = DataAuthTypeEnum.ONLY_ONESELF.getValue();

    /**
     * 数据权限
     */
    @Column(name = "data_auths", columnDefinition = "text comment '数据权限'")
    private String dataAuths;
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

}
