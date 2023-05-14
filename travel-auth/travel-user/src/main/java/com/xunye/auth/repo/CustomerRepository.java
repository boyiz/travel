package com.xunye.auth.repo;

import com.xunye.auth.entity.Customer;
import com.xunye.auth.entity.QCustomer;
import com.xunye.core.base.BaseRepository;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, String>,
        QuerydslBinderCustomizer<QCustomer> {

    @Override
    default void customize(QuerydslBindings bindings, QCustomer root) {
        // todo： 配置web参数映射规则
    }

}
