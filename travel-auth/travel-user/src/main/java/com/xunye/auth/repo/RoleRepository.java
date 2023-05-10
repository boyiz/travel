package com.xunye.auth.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.auth.entity.QRole;
import com.xunye.auth.entity.Role;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, String>,
        QuerydslBinderCustomizer<QRole> {

    @Override
    default void customize(QuerydslBindings bindings, QRole root) {
        bindings.bind(root.roleName).first(StringExpression::contains);
    }

}
