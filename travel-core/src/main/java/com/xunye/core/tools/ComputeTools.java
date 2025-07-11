package com.xunye.core.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import cn.hutool.core.util.NumberUtil;
import org.assertj.core.util.Lists;


public class ComputeTools {
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    //这个类不能实例化
    private ComputeTools() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param v1 被乘数
     * @param v2 乘数
     * @return 两个参数的积
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    public static double div(double v1, double v2) {
        return div(v1, v2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {

        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static int compare(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.compareTo(b2);
    }

    public static Integer mul100(String v) {
        if (v == null) {
            return 0;
        }
        BigDecimal b = new BigDecimal(v);
        b.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal b2 = new BigDecimal(100);
        return b.multiply(b2).intValue();
    }

    /*立方米转立方厘米，精确到小数点后6位*/
    public static Integer cubicM2CM(String v) {
        if (v == null) {
            return 0;
        }
        BigDecimal b = new BigDecimal(v);
        b.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal b2 = new BigDecimal(100 * 100 * 100);
        return b.multiply(b2).intValue();
    }

    public static String toStr(BigDecimal v) {
        if (v == null) {
            return "0";
        }
        return NumberUtil.toStr(v);
    }

    public static String toStr(Integer v) {
        if (v == null) {
            return "0";
        }
        return NumberUtil.toStr(v);
    }

    /**
     * 倒挤算法实现尾差平衡
     * @param totalValue  总值
     * @param count       个数
     * @param scale       小数位数
     * @return 结果list
     */
    public static List<String> backSqueeze(String totalValue,int count, int scale) {
        if (count <= 0) {
            return new ArrayList<>();
        }else if (count == 1) {
            return Lists.newArrayList(totalValue);
        }

        // 对默认值做处理
        String totalValueCopy = totalValue;
        if (CheckTools.isNullOrEmpty(totalValueCopy)) {
            totalValueCopy = "0";
        }

        // 四舍五入 得到平均值
        String avgValueStr = NumberUtil.roundStr(
                NumberUtil.div(totalValue, String.valueOf(count)).toString(),
                scale
        );
        // 前(count-1)项的和
        String exceptLastItemTotalValue = NumberUtil.mul(String.valueOf(count - 1), avgValueStr).toString();

        // 若前(count - 1)项的和 >= 总值；采用向下取整的方式重新算一遍
        if (NumberUtil.toBigDecimal(exceptLastItemTotalValue).compareTo(NumberUtil.toBigDecimal(totalValue)) >= 0) {
            // 向下舍位 得到平均值
            avgValueStr = NumberUtil.roundStr(
                    NumberUtil.div(totalValue, String.valueOf(count)).toString(),
                    scale,
                    RoundingMode.DOWN
            );
            // 前(count-1)项的和
            exceptLastItemTotalValue = NumberUtil.mul(String.valueOf(count - 1), avgValueStr).toString();
        }
        // 最后一项的值
        String lastItemValue = NumberUtil.sub(totalValue,exceptLastItemTotalValue).toString();

        List<String> targetValues = new ArrayList<>();
        for (int i = 0; i < count-1; i++) {
            targetValues.add(avgValueStr);
        }
        targetValues.add(lastItemValue);
        return targetValues;
    }

    //public static void main(String[] args) {
    //    String test = "50.89";
    //    System.out.println(qwe(test,3,2));;
    //
    //    String test1 = "100";
    //    System.out.println(qwe(test, 60, 0));
    //}

}
