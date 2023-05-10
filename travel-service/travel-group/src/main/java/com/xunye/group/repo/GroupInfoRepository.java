package com.xunye.group.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.group.entity.GroupInfo;
import com.xunye.group.entity.QGroupInfo;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupInfoRepository extends BaseRepository<GroupInfo, String>,
        QuerydslBinderCustomizer<QGroupInfo> {

    @Override
    default void customize(QuerydslBindings bindings, QGroupInfo root) {
        // todo： 配置web参数映射规则
    }

}
