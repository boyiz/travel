package com.xunye.core.tools;

import java.util.Date;

import cn.hutool.extra.pinyin.PinyinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdTools {

    @Autowired
    private RedisUtil redisUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger(IdTools.class);

    public String generateId(String key) {
        if (CheckTools.isNullOrEmpty(key)) {
            return null;
        }
        if (PinyinUtil.isChinese(key.charAt(0))) {
            key = PinyinUtil.getFirstLetter(key, "").toUpperCase();
        }
        String firstHalf = key + DateTools.formatDate(new Date(), "yyMMdd");
        Object lastHalfObj = redisUtil.hget(key, firstHalf);
        Integer lastHalf = 0;
        if (lastHalfObj == null) {
            LOGGER.error("从redis未获取到id {}，准备新建", firstHalf);
            boolean ret = redisUtil.hset(key, firstHalf, 1, 86400);
            //redis出错了
            if (!ret) {
                LOGGER.error("redis访问失效. {}", firstHalf);
                return null;
            }
            //redis正常
            else
                lastHalf = 1;
        } else {
            LOGGER.info("从redis获取到id {}, {}", firstHalf, lastHalfObj.toString());
            lastHalf = Integer.valueOf(lastHalfObj.toString());
        }

        String identifier = firstHalf + String.format("%04d", lastHalf);

        redisUtil.hincr(key, firstHalf, 1);

        LOGGER.info("firstHalf {}, lastHalf {}, identifier {}", firstHalf, lastHalf, identifier);

        return identifier;
    }

}
