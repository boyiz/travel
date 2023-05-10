package com.xunye.order.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.entity.QOrderInfo;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInfoRepository extends BaseRepository<OrderInfo, String>,
        QuerydslBinderCustomizer<QOrderInfo> {

    @Override
    default void customize(QuerydslBindings bindings, QOrderInfo root) {
        // todo： 配置web参数映射规则
    }

}
