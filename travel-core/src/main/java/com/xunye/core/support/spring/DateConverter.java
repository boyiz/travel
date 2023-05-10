package com.xunye.core.support.spring;

import java.util.Date;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String value) {
        if (StringUtils.isNoneBlank(value)) {
            DateTime parse = DateUtil.parse(value.trim());
            log.info(parse.toDateStr());
            return parse;
        }
        return null;
    }

}
