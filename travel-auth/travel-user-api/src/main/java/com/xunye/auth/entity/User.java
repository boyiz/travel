package com.xunye.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import com.xunye.core.em.UserTypeEnum;
import com.xunye.core.tools.CheckTools;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 * @ClassName User
 * @Description 用户实体
 * @Author boyiz
 * @Date 2023/4/10 11:45
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = User.TABLE_NAME)
@SQLDelete(sql = "update " + User.TABLE_NAME + " set is_delete = '1' where id = ?")
@Where(clause = "is_delete = 0")
@NoArgsConstructor
public class User extends BaseEntity {

    public static final String TABLE_NAME = "sys_user_info";

    @Column(name="user_name", columnDefinition = "varchar(32) comment '用户名'")
    private String userName;

    @Column(name = "nick_name", columnDefinition = "varchar(32) comment '用户昵称'")
    private String nickName;

    @Column(name = "password", columnDefinition = "varchar(255) comment '用户密码'")
    private String password;

    @Column(name = "user_address", columnDefinition = "varchar(255) comment '用户地址'")
    private String address;

    @Column(name = "user_street", columnDefinition = "varchar(255) comment '用户地址街道'")
    private String street;

    @Column(name = "avatar_url", columnDefinition = "text comment '用户头像'")
    private String avatarUrl;

    @Column(name = "user_birthday", columnDefinition = "datetime(3) comment '用户生日'")
    private Date birthday;

    @Column(name = "user_email", columnDefinition = "varchar(64) comment '用户邮箱'")
    private String email;

    @Column(name = "user_gender", columnDefinition = "int comment '用户性别'")
    private Integer gender;

    @Column(name = "identity_code", columnDefinition = "varchar(32) comment '用户身份信息'")
    private String identityCode;

    @Column(name = "identity_type", columnDefinition = "int default '0' comment '用户身份类型'")
    private Integer identityType;

    @Column(name = "user_phone", columnDefinition = "varchar(16) comment '用户手机号'")
    private String phone;

    @Column(name = "account_status", columnDefinition = "int default '0' comment '账号状态'")
    private Integer status;

    @Column(name = "account_type", columnDefinition = "int default '0' comment '账号类型'")
    private Integer type = UserTypeEnum.NORMAL_USER.getValue();

    /**
     * 用户微信openid
     */
    @Column(name = "user_openid", columnDefinition = "varchar(255) comment '用户微信openid'")
    private String userOpenid;

    /**
     * 用户在开放平台的唯一标识符
     */
    @Column(name = "user_unionid", columnDefinition = "varchar(255) comment '用户开放平台唯一标识'")
    private String userUnionid;

    @Column(name = "is_delete", columnDefinition = "int comment '是否删除'")
    private Integer isDelete = 0;

    @Column(name = "dept_id", columnDefinition = "varchar(32) comment '部门ID'")
    private String deptId = "1";
    /**
     * 部门名
     */
    @Column(name = "dept_name", columnDefinition = "varchar(32) comment '部门名'")
    private String deptName;

    public User(Date date) {
        setUpdateTime(date);
    }

    /**
     * 判断是否是超级管理员
     */
    public Boolean judgeIfSuperAdmin() {
        if (CheckTools.isNotNullOrEmpty(type) && type.equals(UserTypeEnum.SUPER_ADMIN.getValue())) {
            return true;
        }
        return false;
    }
}
