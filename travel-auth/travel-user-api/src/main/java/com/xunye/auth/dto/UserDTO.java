package com.xunye.auth.dto;

import cn.hutool.core.lang.Assert;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xunye.core.tools.CheckTools;
import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 用户实体DTO
 *
 * @Author: boyiz
 * @Date: 2023-04-11
 */
@Data
@ColumnWidth(value = 25)
public class UserDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "用户名")
    private String userName;

    @ExcelProperty(value = "用户微信openid")
    @JsonIgnore
    private String userOpenid;

    @ExcelProperty(value = "用户地址")
    private String address;

    @ExcelProperty(value = "用户头像")
    private String avatarUrl;

    @ExcelProperty(value = "用户生日")
    private Date birthday;

    @ExcelProperty(value = "用户邮箱")
    private String email;

    @ExcelProperty(value = "用户性别")
    private Long gender;

    @ExcelProperty(value = "用户身份信息")
    private String identityCode;

    @ExcelProperty(value = "用户身份类型")
    private Long identityType;

    @ExcelProperty(value = "用户昵称")
    private String nickName;

    @JsonIgnore
    @ExcelProperty(value = "用户密码")
    private String password;

    @ExcelProperty(value = "用户手机号")
    private String phone;

    @ExcelProperty(value = "账号状态")
    @JsonIgnore
    private Long status;

    @ExcelProperty(value = "用户地址街道")
    private String street;

    @ExcelProperty(value = "账号类型")
    @JsonIgnore
    private Long type;

    @ExcelProperty(value = "用户开放平台唯一标识")
    @JsonIgnore
    private String userUnionid;

    @JsonIgnore
    @ExcelProperty(value = "部门ID")
    private String deptId;

    @JsonIgnore
    @ExcelProperty(value = "部门名")
    private String deptName;

    @ExcelProperty(value = "创建人ID")
    @JsonIgnore
    private String createBy;

    @ExcelProperty(value = "创建人员")
    @JsonIgnore
    private String createByName;

    @ExcelProperty(value = "创建时间")
    @JsonIgnore
    private Date createTime;

    @ExcelProperty(value = "修改人ID")
    @JsonIgnore
    private String updateBy;

    @ExcelProperty(value = "修改人员")
    @JsonIgnore
    private String updateByName;

    @ExcelProperty(value = "修改时间")
    @JsonIgnore
    private Date updateTime;

    @ExcelIgnore
    private String token;


}
