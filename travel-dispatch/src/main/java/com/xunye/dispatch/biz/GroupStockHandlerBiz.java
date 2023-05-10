package com.xunye.dispatch.biz;

import com.xunye.order.event.GroupStockUpdateEvent;

/**
 * @ClassName GroupStockHandlerBiz
 * @Description
 * @Author boyiz
 * @Date 2023/5/9 11:37
 * @Version 1.0
 **/

public interface GroupStockHandlerBiz {

    void onGroupStockUpdate(GroupStockUpdateEvent event);
}
