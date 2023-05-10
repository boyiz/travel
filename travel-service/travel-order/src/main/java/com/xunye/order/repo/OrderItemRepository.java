package com.xunye.order.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.order.entity.OrderItem;
import com.xunye.order.entity.QOrderItem;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends BaseRepository<OrderItem, String>,
        QuerydslBinderCustomizer<QOrderItem> {

    @Override
    default void customize(QuerydslBindings bindings, QOrderItem root) {
        // todo： 配置web参数映射规则
    }

}
