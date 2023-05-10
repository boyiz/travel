package com.xunye.route.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.route.entity.RouteSchedule;
import com.xunye.route.entity.QRouteSchedule;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteScheduleRepository extends BaseRepository<RouteSchedule, String>,
        QuerydslBinderCustomizer<QRouteSchedule> {

    @Override
    default void customize(QuerydslBindings bindings, QRouteSchedule root) {
        // todo： 配置web参数映射规则
    }

}
