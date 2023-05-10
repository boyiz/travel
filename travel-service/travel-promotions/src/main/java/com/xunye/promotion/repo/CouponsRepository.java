package com.xunye.promotion.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.promotion.entity.Coupons;
import com.xunye.promotion.entity.QCoupons;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponsRepository extends BaseRepository<Coupons, String>,
        QuerydslBinderCustomizer<QCoupons> {

    @Override
    default void customize(QuerydslBindings bindings, QCoupons root) {
        // todo： 配置web参数映射规则
    }

}
