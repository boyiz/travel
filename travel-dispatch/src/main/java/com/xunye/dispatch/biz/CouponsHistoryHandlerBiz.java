package com.xunye.dispatch.biz;

import com.xunye.order.event.CouponsHistoryUpdateEvent;

/**
 * @ClassName PolicyHistoryHandlerBiz
 * @Description
 * @Author boyiz
 * @Date 2023/5/2 23:18
 * @Version 1.0
 **/
public interface CouponsHistoryHandlerBiz {

    void onUserOrderUpdateCouponsHistory(CouponsHistoryUpdateEvent event);
}
