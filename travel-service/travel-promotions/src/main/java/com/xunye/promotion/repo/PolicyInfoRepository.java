package com.xunye.promotion.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.promotion.entity.PolicyInfo;
import com.xunye.promotion.entity.QPolicyInfo;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface PolicyInfoRepository extends BaseRepository<PolicyInfo, String>,
        QuerydslBinderCustomizer<QPolicyInfo> {

    @Override
    default void customize(QuerydslBindings bindings, QPolicyInfo root) {
        // todo： 配置web参数映射规则
    }

}
