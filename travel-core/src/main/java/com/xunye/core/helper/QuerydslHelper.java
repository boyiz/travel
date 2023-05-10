package com.xunye.core.helper;

import java.util.Date;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.core.types.dsl.StringTemplate;
import com.xunye.core.base.BaseEnum;

/**
 * Querydsl帮助类
 * 扩展、辅助一些操作
 */
public class QuerydslHelper {

    /**
     * find_in_set模板字符串
     * {0} 检索项
     * {1} 检索目标
     */
    public static final String TPL_FIND_IN_SET = "FIND_IN_SET({0},{1})";
    /**
     * YEAR函数
     * 年
     */
    public static final String TPL_YEAR = "YEAR({0})";
    /**
     * QUARTER函数
     * 季度
     */
    public static final String TPL_QUARTER = "QUARTER({0})";
    /**
     * MONTH函数
     * 月份
     */
    // public static final String TPL_MONTH = "Right(100 + Month({0}), 2)";
    public static final String TPL_MONTH = "Month({0})";

    /**
     * IFNULL函数
     * {0} 字段
     * {1} 默认值
     */
    public static final String TPL_IF_NULL = "IFNULL({0},{1})";

    /**
     * group_concat函数
     * {0} 字段
     */
    public static final String TPL_GROUP_CONCAT = "group_concat({0})";


    /**
     * 简化QueryDsl调用FIND_IN_SET
     *
     * @param search    检索项
     * @param fieldPath Q字段
     * @return BooleanExpression
     */
    public static BooleanExpression findInSet(String search, StringPath fieldPath) {
        return Expressions.numberTemplate(
                Integer.class,
                TPL_FIND_IN_SET,
                search, fieldPath
        ).gt(0);
    }

    /**
     * 简化QueryDsl调用GROUP_CONCAT
     *
     * @param fieldPath Q字段
     * @return BooleanExpression
     */
    public static StringExpression groupConcat(StringPath fieldPath) {
        return Expressions.stringTemplate(
                TPL_GROUP_CONCAT,
                fieldPath
        );
    }

    /**
     * 简化调用YEAR函数
     *
     * @param fieldPath Q字段
     * @return StringTemplate
     */
    public static StringTemplate year(DateTimePath<Date> fieldPath) {
        return Expressions.stringTemplate(TPL_YEAR, fieldPath);
    }

    /**
     * 简化调用QUARTER函数
     *
     * @param fieldPath Q字段
     * @return StringTemplate
     */
    public static StringTemplate quarter(DateTimePath<Date> fieldPath) {
        return Expressions.stringTemplate(TPL_QUARTER, fieldPath);
    }

    /**
     * 简化调用MONTH函数
     *
     * @param fieldPath Q字段
     * @return StringTemplate
     */
    public static StringTemplate month(DateTimePath<Date> fieldPath) {
        return Expressions.stringTemplate(TPL_MONTH, fieldPath);
    }

    /**
     * 仅适用于BaseEnum的When-Then映射
     * 形如：
     * // StringExpression titleColExpression = new CaseBuilder()
     * //         .when(qDailyPayment.paymentType.eq(DailyPaymentTypeEnum.DRINKING_WATER.getValue())).then(DailyPaymentTypeEnum.DRINKING_WATER.getLabel())
     * //         .when(qDailyPayment.paymentType.eq(DailyPaymentTypeEnum.CLEANING_FEE.getValue())).then(DailyPaymentTypeEnum.CLEANING_FEE.getLabel())
     * //         .when(qDailyPayment.paymentType.eq(DailyPaymentTypeEnum.COURIER_FEE.getValue())).then(DailyPaymentTypeEnum.COURIER_FEE.getLabel())
     * //         .when(qDailyPayment.paymentType.eq(DailyPaymentTypeEnum.OFFICE_SUPPLIES.getValue())).then(DailyPaymentTypeEnum.OFFICE_SUPPLIES.getLabel())
     * //         .when(qDailyPayment.paymentType.eq(DailyPaymentTypeEnum.COMPUTER_EQUIPMENT.getValue())).then(DailyPaymentTypeEnum.COMPUTER_EQUIPMENT.getLabel())
     * //         .otherwise(DailyPaymentTypeEnum.OTHER.getLabel());
     *
     * @param path
     * @param baseEnums
     * @return
     */
    public static StringExpression buildCaseWhenThenByEnum(NumberPath<Integer> path, BaseEnum[] baseEnums) {
        BaseEnum firstEnum = baseEnums[0];
        CaseBuilder.Cases<String, StringExpression> thenResult = new CaseBuilder()
                .when(path.eq(firstEnum.getValue())).then(firstEnum.getLabel());
        if (baseEnums.length > 1) {
            for (int i = 1; i < baseEnums.length; i++) {
                thenResult = thenResult.when(path.eq(baseEnums[i].getValue())).then(baseEnums[i].getLabel());
            }
        }
        return thenResult.otherwise("");
    }

}
