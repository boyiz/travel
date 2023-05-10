package com.xunye.notice.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.notice.entity.RouteNotice;
import com.xunye.notice.entity.QRouteNotice;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteNoticeRepository extends BaseRepository<RouteNotice, String>,
        QuerydslBinderCustomizer<QRouteNotice> {

    @Override
    default void customize(QuerydslBindings bindings, QRouteNotice root) {
        // todo： 配置web参数映射规则
    }

}
