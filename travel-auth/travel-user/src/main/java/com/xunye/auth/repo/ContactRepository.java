package com.xunye.auth.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.auth.entity.Contact;
import com.xunye.auth.entity.QContact;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends BaseRepository<Contact, String>,
        QuerydslBinderCustomizer<QContact> {

    @Override
    default void customize(QuerydslBindings bindings, QContact root) {
        // todo： 配置web参数映射规则
    }

}
