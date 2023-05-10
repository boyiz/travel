package com.xunye.order.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.order.entity.OrderUserRelation;
import com.xunye.order.entity.QOrderUserRelation;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderUserRelationRepository extends BaseRepository<OrderUserRelation, String>,
        QuerydslBinderCustomizer<QOrderUserRelation> {

    @Override
    default void customize(QuerydslBindings bindings, QOrderUserRelation root) {
        // todo： 配置web参数映射规则
    }

}
