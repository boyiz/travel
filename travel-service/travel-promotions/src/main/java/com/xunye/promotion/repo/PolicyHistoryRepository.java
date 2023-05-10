package com.xunye.promotion.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.promotion.entity.PolicyHistory;
import com.xunye.promotion.entity.QPolicyHistory;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyHistoryRepository extends BaseRepository<PolicyHistory, String>,
        QuerydslBinderCustomizer<QPolicyHistory> {

    @Override
    default void customize(QuerydslBindings bindings, QPolicyHistory root) {
        // todo： 配置web参数映射规则
    }

}
