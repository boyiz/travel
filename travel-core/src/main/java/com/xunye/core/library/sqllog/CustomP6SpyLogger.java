//package com.xunye.core.library.sqllog;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.db.sql.SqlUtil;
//import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class CustomP6SpyLogger implements MessageFormattingStrategy {
//
//    @Override
//    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("==========================================================================\n")
//                .append("连接id：").append(connectionId).append("\n")
//                .append("类别：").append(category).append("\n")
//                .append("当前时间：").append(DateUtil.now()).append("\n")
//                .append("花费时间：").append(elapsed).append("ms").append("\n");
//        if (category.equals("statement")) {
//            sb.append("目标SQL(包含参数)：\n")
//                    .append(SqlUtil.formatSql(sql))
//                    .append("\n==========================================================================\n");
//
//        } else {
//            sb.append("==========================================================================\n");
//        }
//        return sb.toString();
//    }
//
//}
