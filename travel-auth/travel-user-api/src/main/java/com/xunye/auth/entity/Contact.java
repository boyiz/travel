package com.xunye.auth.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xunye.core.base.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;


/**
 * @ClassName Contact
 * @Description 用户关联出行人
 * @Author boyiz
 * @Date 2023/5/4 09:15
 * @Version 1.0
 **/

@Data
@Entity
@DynamicUpdate
@Table(name = Contact.TABLE_NAME)
public class Contact extends BaseEntity {

    public static final String TABLE_NAME = "contact";

    @Column(name="user_name", columnDefinition = "varchar(32) comment '用户名'")
    private String userName;

    @Column(name = "identity_code", columnDefinition = "varchar(32) comment '用户身份信息'")
    private String identityCode;

    @Column(name = "identity_type", columnDefinition = "int default '0' comment '用户身份类型'")
    private Integer identityType;

    @Column(name = "user_birthday", columnDefinition = "datetime(3) comment '用户生日'")
    private Date birthday;

    @Column(name = "user_gender", columnDefinition = "int comment '用户性别'")
    private Integer gender;




    @Column(name = "user_phone", columnDefinition = "varchar(16) comment '用户手机号'")
    private String phone;

    @Column(name = "user_email", columnDefinition = "varchar(64) comment '用户邮箱'")
    private String email;

    @Column(name = "user_address", columnDefinition = "varchar(255) comment '用户地址'")
    private String address;

    @Column(name = "user_street", columnDefinition = "varchar(255) comment '用户地址街道'")
    private String street;


    @Column(name = "user_id", columnDefinition = "varchar(255) comment '关联的用户id'")
    private String userId;

    @Column(name = "is_emergency", columnDefinition = "varchar(255) comment '紧急联系人'")
    private Integer isEmergency;

}
