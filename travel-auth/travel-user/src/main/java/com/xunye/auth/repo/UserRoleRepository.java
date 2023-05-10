package com.xunye.auth.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.auth.entity.UserRole;
import com.xunye.auth.entity.QUserRole;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, String>,
        QuerydslBinderCustomizer<QUserRole> {

    @Override
    default void customize(QuerydslBindings bindings, QUserRole root) {
        // todo： 配置web参数映射规则
    }

}
