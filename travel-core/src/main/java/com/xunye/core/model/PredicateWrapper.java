package com.xunye.core.model;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.Data;

/**
 * 1.对原生的predicate包装一层
 * 2.后期方便拓展
 */
@Data
public class PredicateWrapper {

    private Predicate predicate;
    private String other;

    public PredicateWrapper(Predicate predicate, String other) {
        this.predicate = predicate;
        this.other = other;
    }

    public static PredicateWrapper of(Predicate predicate) {
        return new PredicateWrapper(predicate, null);
    }

    public static PredicateWrapper of() {
        return of(new BooleanBuilder());
    }

}
