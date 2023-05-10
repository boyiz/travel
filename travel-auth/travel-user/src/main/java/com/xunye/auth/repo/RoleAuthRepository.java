package com.xunye.auth.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.auth.entity.RoleAuth;
import com.xunye.auth.entity.QRoleAuth;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleAuthRepository extends BaseRepository<RoleAuth, String>,
        QuerydslBinderCustomizer<QRoleAuth> {

    @Override
    default void customize(QuerydslBindings bindings, QRoleAuth root) {
        // todo： 配置web参数映射规则
    }

}
