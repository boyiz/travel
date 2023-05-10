package com.xunye.core.support.orm;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * 自定义MySQL方言
 */
@Slf4j
public class CustomMysqlDialect extends MySQL8Dialect {

    public CustomMysqlDialect() {
        super();
        // 注册right函数
        registerFunction("right", new StandardSQLFunction("right", StandardBasicTypes.STRING));

        registerFunction("date", new StandardSQLFunction("date", StandardBasicTypes.STRING));

        registerFunction("date_format", new StandardSQLFunction("date_format", StandardBasicTypes.STRING));
        registerFunction("datediff", new StandardSQLFunction("datediff", StandardBasicTypes.INTEGER));
        registerFunction("diff_days", new StandardSQLFunction("diff_days", StandardBasicTypes.STRING));
        registerFunction("group_concat", new StandardSQLFunction("group_concat(?1)", StandardBasicTypes.STRING));
        registerFunction("group_concat", new SQLFunctionTemplate(StandardBasicTypes.STRING, "group_concat(?1)"));

        log.info("数据库方言注册完毕[CustomMysqlDialect]");
    }

}
