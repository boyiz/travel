package com.xunye.auth.repo;

import com.xunye.auth.entity.QUser;
import com.xunye.auth.entity.User;
import com.xunye.core.base.BaseRepository;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, String>,
        QuerydslBinderCustomizer<QUser> {

    @Override
    default void customize(QuerydslBindings bindings, QUser root) {
        // todo： 配置web参数映射规则
    }

}
