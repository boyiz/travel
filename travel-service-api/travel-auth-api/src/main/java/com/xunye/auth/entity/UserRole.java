package com.xunye.auth.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = UserRole.TABLE_NAME)
public class UserRole implements Serializable {

    public static final String TABLE_NAME = "user_role";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "varchar(32) comment '用户ID'")
    private String userId;

    @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'")
    private String roleId;

}