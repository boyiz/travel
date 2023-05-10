package com.xunye.core.support.orm.interceptor;

public interface IOrmInterceptor {

    /**
     * 处理sql
     */
    String convertSql(String originalSql);

}
