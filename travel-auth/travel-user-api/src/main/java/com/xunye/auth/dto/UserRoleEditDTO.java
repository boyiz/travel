package com.xunye.auth.dto;

import lombok.Data;
import java.util.Date;

/**
 * 用户角色实体新增/编辑DTO
 *
 * @Author: boyiz
 * @Date: 2023-05-08
 */
@Data
public class UserRoleEditDTO {

    /*  */
    private Long id;
    /* 用户ID */
    private String userId;
    /* 角色ID */
    private String roleId;

}
