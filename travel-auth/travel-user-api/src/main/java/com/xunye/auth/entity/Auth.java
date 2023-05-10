package com.xunye.auth.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;

/**
 * @ClassName Auth
 * @Description 权限实体
 * @Author boyiz
 * @Date 2023/4/19 15:18
 * @Version 1.0
 **/

@Data
@Entity
@Table(name = Auth.TABLE_NAME)
public class Auth extends BaseEntity {

    public static final String TABLE_NAME = "auth";

    /**
     * 权限名称
     */
    @Column(name = "auth_name", columnDefinition = "varchar(255) comment '权限名称'")
    private String authName;
    /**
     * 权限标识
     */
    @Column(name = "auth_key", columnDefinition = "varchar(100) comment '权限标识'")
    private String authKey;
    /**
     * 权限类型
     */
    @Column(name = "auth_type", columnDefinition = "int comment '权限类型'")
    private Integer authType;
    /**
     * 接口地址
     */
    @Column(name = "path", columnDefinition = "varchar(255) comment '接口地址'")
    private String path;
    /**
     * 请求方式
     */
    @Column(name = "request_method", columnDefinition = "varchar(50) comment '请求方式'")
    private String requestMethod;
    /**
     * 前端路由
     */
    @Column(name = "route_path", columnDefinition = "varchar(255) comment '前端路由'")
    private String routePath;
    /**
     * 是否可见
     * 0 否
     * 1 是
     */
    @Column(name = "is_show", columnDefinition = "tinyint comment '是否可见'")
    private Boolean isShow;
    /**
     * 图标
     */
    @Column(name = "icon", columnDefinition = "varchar(255) comment '图标'")
    private String icon;
    /**
     * 父权限ID
     */
    @Column(name = "parent_id", columnDefinition = "varchar(32) comment '父权限ID'")
    private String parentId;
    /**
     * 排序号
     */
    @Column(name = "sort_no", columnDefinition = "int comment '排序号'")
    private Integer sortNo;
    /**
     * Antd配置项
     */
    @Column(name = "antd_settings", columnDefinition = "text comment 'Antd配置项'")
    private String antdSettings;

}
