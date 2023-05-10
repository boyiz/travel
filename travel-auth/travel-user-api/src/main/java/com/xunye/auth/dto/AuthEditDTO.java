package com.xunye.auth.dto;

import lombok.Data;

/**
 * 权限新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-19
 */
@Data
public class AuthEditDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 权限名称
     */
    private String authName;
    /**
     * 权限标识
     */
    private String authKey;
    /**
     * 权限类型
     */
    private Integer authType;
    /**
     * 接口地址
     */
    private String path;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 前端路由
     */
    private String routePath;
    /**
     * 是否可见
     * 0 否
     * 1 是
     */
    private Boolean isShow;
    /**
     * 图标
     */
    private String icon;
    /**
     * 父权限ID
     */
    private String parentId;
    /**
     * 排序号
     */
    private Integer sortNo;
    /**
     * Antd配置项
     */
    private String antdSettings;

}
