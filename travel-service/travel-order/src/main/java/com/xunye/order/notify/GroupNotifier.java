package com.xunye.order.notify;

import javax.annotation.Resource;

import com.xunye.order.event.GroupStockUpdateEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @ClassName GroupNotifier
 * @Description
 * @Author boyiz
 * @Date 2023/5/9 11:29
 * @Version 1.0
 **/

@Component
public class GroupNotifier {

    @Resource
    private ApplicationContext applicationContext;

    public void updateGroupStock(String groupId, Integer number) {
        applicationContext.publishEvent(new GroupStockUpdateEvent(this, groupId,number));
    }
}
