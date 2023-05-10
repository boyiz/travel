package com.xunye.auth.repo;

import com.xunye.core.base.BaseRepository;
import com.xunye.auth.entity.Dept;
import com.xunye.auth.entity.QDept;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptRepository extends BaseRepository<Dept, String>,
        QuerydslBinderCustomizer<QDept> {

    @Override
    default void customize(QuerydslBindings bindings, QDept root) {
        bindings.bind(root.deptName, root.leaderUserNames).first(StringExpression::contains);
    }

}
