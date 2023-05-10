package com.xunye.route.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.route.entity.RouteImage;
import com.xunye.route.entity.QRouteImage;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteImageRepository extends BaseRepository<RouteImage, String>,
        QuerydslBinderCustomizer<QRouteImage> {

    @Override
    default void customize(QuerydslBindings bindings, QRouteImage root) {
        // todo： 配置web参数映射规则
    }

}
