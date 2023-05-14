package com.xunye.auth.dto;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xunye.core.tools.CheckTools;
import lombok.Data;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 用户实体新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-11
 */
@Data
public class UserEditDTO {

    /* id */
    private String id;
    /* username */
    private String userName;
    /* 用户微信openid */
    @JsonIgnore
    private String userOpenid;
    /* 用户地址 */
    private String address;
    /* 用户头像 */
    private String avatarUrl;
    /* 用户生日 */
    private Date birthday;
    /* 用户邮箱 */
    private String email;
    /* 用户性别 */
    private Long gender;
    /* 用户身份信息 */
    private String identityCode;
    /* 用户身份类型 */
    private Long identityType;
    /* 用户昵称 */
    private String nickName;
    /* 用户密码 */
    private String password;
    /* 用户手机号 */
    private String phone;
    /* 账号状态 */
    private Long status;
    /* 用户地址街道 */
    private String street;
    /* 账号类型 */
    private Long type;
    /* 用户开放平台唯一标识 */
    private String userUnionid;
    /* 部门ID */
    private String deptId;
    /* 部门名 */
    private String deptName;
    /* 创建人ID */
    @JsonIgnore
    private String createBy;
    /* 创建人员 */
    @JsonIgnore
    private String createByName;
    /* 创建时间 */
    @JsonIgnore
    private Date createTime;
    /* 修改人ID */
    @JsonIgnore
    private String updateBy;
    /* 修改人员 */
    @JsonIgnore
    private String updateByName;
    /* 修改时间 */
    @JsonIgnore
    private Date updateTime;

    public void check() {
        Assert.isTrue(CheckTools.isNotNullOrEmpty(userName), "用户名为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(password), "密码为空");
    }
}
