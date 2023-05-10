package com.xunye.promotion.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.promotion.entity.CouponsHistory;
import com.xunye.promotion.entity.QCouponsHistory;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponsHistoryRepository extends BaseRepository<CouponsHistory, String>,
        QuerydslBinderCustomizer<QCouponsHistory> {

    @Override
    default void customize(QuerydslBindings bindings, QCouponsHistory root) {
        // todo： 配置web参数映射规则
    }

}
