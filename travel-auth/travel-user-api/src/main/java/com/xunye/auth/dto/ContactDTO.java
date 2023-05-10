package com.xunye.auth.dto;

import lombok.Data;
import com.xunye.core.base.BaseDTO;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;

/**
 * 关联出行人DTO
 *
 * @Author: boyiz
 * @Date: 2023-05-04
 */
@Data
@ColumnWidth(value = 25)
public class ContactDTO extends BaseDTO {

    @ExcelProperty(value = "id")
    private String id;

    @ExcelProperty(value = "用户名")
    private String userName;

    @ExcelProperty(value = "用户身份信息")
    private String identityCode;

    @ExcelProperty(value = "用户身份类型")
    private Long identityType;

    @ExcelProperty(value = "用户生日")
    private Date userBirthday;

    @ExcelProperty(value = "用户性别")
    private Long userGender;

    @ExcelProperty(value = "用户手机号")
    private String userPhone;

    @ExcelProperty(value = "用户邮箱")
    private String userEmail;

    @ExcelProperty(value = "用户地址")
    private String userAddress;

    @ExcelProperty(value = "用户地址街道")
    private String userStreet;

    @ExcelProperty(value = "关联的用户id")
    private String userId;

    @ExcelProperty(value = "紧急联系人")
    private String isEmergency;

    @ExcelProperty(value = "创建人ID")
    private String createBy;

    @ExcelProperty(value = "创建人员")
    private String createByName;

    @ExcelProperty(value = "创建时间")
    private Date createTime;

    @ExcelProperty(value = "修改人ID")
    private String updateBy;

    @ExcelProperty(value = "修改人员")
    private String updateByName;

    @ExcelProperty(value = "修改时间")
    private Date updateTime;


}
