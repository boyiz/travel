package com.xunye.core.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.common.base.Splitter;

public class StringTools {

    /**
     * 截取字符串的后length位
     *
     * @param str    目标字符串
     * @param length 位数
     * @return 子串
     */
    public static String cutLastStr(String str, int length) {
        if (length < 0) length = 0;
        if (length > str.length()) {
            return str;
        }
        return str.substring(str.length() - length);
    }

    /**
     * 填充0
     *
     * @param str
     * @param targetNum
     * @return
     */
    public static String zeroFill(String str, int targetNum) {
        if (str.length() < targetNum) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < targetNum - str.length(); i++) {
                stringBuilder.append("0");
            }
            return stringBuilder.append(str).toString();
        }
        return str;
    }

    /**
     * 基于Guava封装split
     * 支出过滤空格和无效分隔符
     *
     * @param str       字符串
     * @param separator 分隔符
     * @return List<String>
     */
    public static List<String> split(String str, String separator) {
        Iterable<String> iterable = Splitter.on(separator).trimResults().omitEmptyStrings().split(str);
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    /**
     * 去空格
     */
    public static String trim(String str) {
        return (str == null ? "" : str.trim());
    }

    /**
     * 是否包含字符串
     *
     * @param str  验证字符串
     * @param strs 字符串组
     * @return 包含返回true
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean equal(String str1, String str2) {
        if (str1 == null || str2 == null)
            return false;
        if (str1 == str2)
            return true;
        return str1.equals(str2);
    }

    public static String add(String str1, String str2, String simbol) {
        String[] strArray = str1.split(simbol);

        List<String> resultList = new ArrayList<>();
        boolean found = false;
        for (String str:strArray) {
            if (str.trim().equals(str2.trim())) {
                found = true;
            }
            resultList.add(str);
        }
        if (!found) {
            resultList.add(str2);
        }
        return String.join(",", resultList);
    }

    public static String delete(String str1, String str2, String simbol) {
        String[] strArray = str1.split(simbol);

        List<String> resultList = new ArrayList<>();
        for (String str:strArray) {
            if (!str.trim().equals(str2.trim())) {
                resultList.add(str);
            }
        }
        return String.join(",", resultList);
    }

    public static String removeSpecialChar(String str){
        String s = "";
        if(str != null){
            // 定义含特殊字符的正则表达式
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            s = m.replaceAll("");
        }
        return s;
    }

    // public static void main(String[] args) {
    //     String workNum = "03060001";
    //     String lastNum = workNum.substring(4);
    //     System.out.println("看看lastNum：" + lastNum);
    //     BigDecimal add = NumberUtil.add(lastNum, "1");
    //     System.out.println("看看add：" + add);
    //     String target = zeroFill("" + add, 4);
    //     System.out.println("看看target：" + target);
    // }

}
