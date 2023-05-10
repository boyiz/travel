package com.xunye.auth.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 角色新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2021-06-29
 */
@Data
public class RoleEditDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 角色标识
     */
    private String roleKey;
    /**
     * 状态
     */
    private Integer roleState;
    /**
     * 数据权限类型
     * 1. 仅自己(默认)
     * 2. 本部门
     * 3. 本部门及其子部门
     * 4. 自定义数据权限
     */
    private Integer dataAuthType;
    /**
     * 数据权限
     */
    private List<String> dataAuths = new ArrayList<>();
    /**
     * 备注
     */
    private String comments;

    /**
     * 角色分配的权限集合
     */
    List<String> menuAuths = new ArrayList<>();

}
