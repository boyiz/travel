package com.xunye.auth.component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.dev33.satoken.stp.StpInterface;
import com.xunye.auth.dto.RoleEditDTO;
import com.xunye.auth.entity.RoleAuth;
import com.xunye.auth.entity.UserRole;
import com.xunye.auth.service.IAuthService;
import com.xunye.auth.service.IRoleService;
import com.xunye.core.tools.CheckTools;
import org.springframework.stereotype.Component;

/**
 * @ClassName StpInterfaceImpl
 * @Description 自定义权限验证接口扩展
 * @Author boyiz
 * @Date 2023/3/18 11:40
 * @Version 1.0
 **/
@Component    // 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private IRoleService roleService;

    @Resource
    private IAuthService authService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        List<String> authIdList = new ArrayList<String>();
        List<String> authKeyList = new ArrayList<String>();

        List<UserRole> userRoleList = roleService.queryUserRoleEditDTOListByUserId(loginId.toString());
        for (UserRole userRole : userRoleList) {
            List<RoleAuth> roleAuthList = authService.queryRoleAuthEditDTOListByRoleId(userRole.getRoleId());
            authIdList.addAll(roleAuthList.stream().map(RoleAuth::getAuthId).collect(Collectors.toList()));
        }
        authIdList.forEach(id ->{
            authKeyList.add(authService.queryAuthById(id).getData().getAuthKey());
        });
        return authKeyList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        List<String> roleKeyList = new ArrayList<>();

        List<UserRole> userRoleList = roleService.queryUserRoleEditDTOListByUserId(loginId.toString());
        userRoleList.forEach(e -> {
            RoleEditDTO roleEditDTO = roleService.queryRoleById(e.getRoleId()).getData();
            if (CheckTools.isNotNullOrEmpty(roleEditDTO)) {
                roleKeyList.add(roleEditDTO.getRoleKey());
            }
        });
        return roleKeyList;
    }

}

