package com.xunye.auth.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.auth.entity.Auth;
import com.xunye.auth.entity.QAuth;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends BaseRepository<Auth, String>,
        QuerydslBinderCustomizer<QAuth> {

    @Override
    default void customize(QuerydslBindings bindings, QAuth root) {
        bindings.bind(
                root.authName,
                root.authKey,
                root.routePath
        ).first(StringExpression::contains);
    }

}
