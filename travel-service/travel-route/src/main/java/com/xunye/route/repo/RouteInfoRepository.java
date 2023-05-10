package com.xunye.route.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.route.entity.RouteInfo;
import com.xunye.route.entity.QRouteInfo;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteInfoRepository extends BaseRepository<RouteInfo, String>,
        QuerydslBinderCustomizer<QRouteInfo> {

    @Override
    default void customize(QuerydslBindings bindings, QRouteInfo root) {
        // todo： 配置web参数映射规则
    }

}
