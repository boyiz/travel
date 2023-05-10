package com.xunye.notice.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.notice.entity.GeneralNotice;
import com.xunye.notice.entity.QGeneralNotice;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralNoticeRepository extends BaseRepository<GeneralNotice, String>,
        QuerydslBinderCustomizer<QGeneralNotice> {

    @Override
    default void customize(QuerydslBindings bindings, QGeneralNotice root) {
        // todo： 配置web参数映射规则
    }

}
