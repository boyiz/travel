package com.xunye.auth.dto;

import com.xunye.core.tools.CheckTools;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 关联出行人新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-05-04
 */
@Data
public class ContactEditDTO {

    /* id */
    private String id;
    /* 用户名 */
    private String userName;
    /* 用户身份信息 */
    private String identityCode;
    /* 用户身份类型 */
    private Long identityType;
    /* 用户生日 */
    private Date userBirthday;
    /* 用户性别 */
    private Long userGender;
    /* 用户手机号 */
    private String userPhone;
    /* 用户邮箱 */
    private String userEmail;
    /* 用户地址 */
    private String userAddress;
    /* 用户地址街道 */
    private String userStreet;
    /* 关联的用户id */
    private String userId;
    /* 紧急联系人 */
    private String isEmergency;
    /* 创建人ID */
    private String createBy;
    /* 创建人员 */
    private String createByName;
    /* 创建时间 */
    private Date createTime;
    /* 修改人ID */
    private String updateBy;
    /* 修改人员 */
    private String updateByName;
    /* 修改时间 */
    private Date updateTime;

    public void check() {
        Assert.isTrue(CheckTools.isNotNullOrEmpty(userId), "关联用户id为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(userName), "姓名为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(identityCode), "证件号为空");
        Assert.isTrue(CheckTools.isNotNullOrEmpty(identityType), "证件类型为空");
    }
}
