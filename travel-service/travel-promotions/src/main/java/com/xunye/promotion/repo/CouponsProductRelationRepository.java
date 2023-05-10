package com.xunye.promotion.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.promotion.entity.CouponsProductRelation;
import com.xunye.promotion.entity.QCouponsProductRelation;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponsProductRelationRepository extends BaseRepository<CouponsProductRelation, String>,
        QuerydslBinderCustomizer<QCouponsProductRelation> {

    @Override
    default void customize(QuerydslBindings bindings, QCouponsProductRelation root) {
        // todo： 配置web参数映射规则
    }

}
