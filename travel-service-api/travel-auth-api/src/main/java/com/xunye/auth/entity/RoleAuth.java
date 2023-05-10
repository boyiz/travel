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
@Table(name = RoleAuth.TABLE_NAME)
public class RoleAuth implements Serializable {

    public static final String TABLE_NAME = "role_auth";
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_id", columnDefinition = "varchar(32) comment '角色ID'")
    private String roleId;

    @Column(name = "auth_id", columnDefinition = "varchar(32) comment '权限ID'")
    private String authId;

}
