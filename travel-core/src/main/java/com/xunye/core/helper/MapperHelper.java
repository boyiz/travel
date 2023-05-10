package com.xunye.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cn.hutool.core.convert.Convert;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.xunye.core.tools.CheckTools;
import com.xunye.core.tools.StringTools;
import org.springframework.stereotype.Component;

@Component
public class MapperHelper {

    /**
     * List<Long>转String
     * 一般用于前端多选，数据库','分割存储场景
     *
     * @param idList id序列集合
     * @return ','拼接后的字符串
     */
    public String listToString(List<String> idList) {
        if (CheckTools.isNullOrEmpty(idList)) return null;
        List<String> strList = idList.stream().map(String::valueOf).collect(Collectors.toList());
        return Joiner.on(",").skipNulls().join(strList);
    }

    /**
     * 用于','拼接字符串的List回显
     *
     * @param strings 字符串
     * @return List<Long>
     */
    public List<String> stringToList(String strings) {
        if (CheckTools.isNullOrEmpty(strings)) return Lists.newArrayList();
        List<String> split = StringTools.split(strings, ",");
        return new ArrayList<>(split);
    }

    public Integer convertStrToInteger(String str) {
        if (CheckTools.isNullOrEmpty(str)) {
            return null;
        }
        return Convert.toInt(str);
    }

    public String convertStrToStr(String str) {
        if (CheckTools.isNullOrEmpty(str)) {
            return null;
        }
        return str;
    }

}
