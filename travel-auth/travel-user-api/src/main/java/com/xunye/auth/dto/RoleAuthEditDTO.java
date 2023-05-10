package com.xunye.auth.dto;

import lombok.Data;
import java.util.Date;

/**
 * 角色权限实体新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-05-08
 */
@Data
public class RoleAuthEditDTO {

    /*  */
    private Long id;
    /* 角色ID */
    private String roleId;
    /* 权限ID */
    private String authId;

}
