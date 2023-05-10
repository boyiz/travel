package com.xunye.core.support.orm.interceptor;

import java.util.Collection;

import com.xunye.core.tools.CheckTools;
import com.xunye.core.tools.SpringTools;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.context.ApplicationContext;

public class OrmBasicInterceptor implements StatementInspector {

    @Override
    public String inspect(String sql) {

        String targetSql = sql + "";
        ApplicationContext applicationContext = SpringTools.getApplicationContext();
        if (CheckTools.isNotNullOrEmpty(applicationContext)) {
            Collection<IOrmInterceptor> ormInterceptors = applicationContext.getBeansOfType(IOrmInterceptor.class).values();
            if (CheckTools.isNotNullOrEmpty(ormInterceptors)) {
                for (IOrmInterceptor ormInterceptor : ormInterceptors) {
                    targetSql = ormInterceptor.convertSql(targetSql);
                }
            }
        }

        return targetSql;
    }

}
