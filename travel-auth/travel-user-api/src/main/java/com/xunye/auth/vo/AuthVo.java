package com.xunye.auth.vo;

import lombok.Data;

@Data
public class AuthVo {

    private String id;
    /* 标识键 */
    private String authKey;
    /* 权限类型 */
    private Integer authType;
    /* 前端路由 */
    private String routePath;
    /* 是否显示 */
    private Boolean isShow;
    /* 接口地址 */
    private String path;
    /* 请求方法 */
    private String requestMethod;

}
