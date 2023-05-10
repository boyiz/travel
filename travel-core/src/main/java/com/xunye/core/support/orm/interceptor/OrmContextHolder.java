package com.xunye.core.support.orm.interceptor;

import org.springframework.util.Assert;

public class OrmContextHolder {

    private static final ThreadLocal<OrmContext> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static OrmContext getContext() {
        OrmContext ctx = contextHolder.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }
        return ctx;
    }

    public static void setContext(OrmContext context) {
        Assert.notNull(context, "Only non-null DataAuthContext instances are permitted");
        contextHolder.set(context);
    }

    public static OrmContext createEmptyContext() {
        return new OrmContext();
    }

}
