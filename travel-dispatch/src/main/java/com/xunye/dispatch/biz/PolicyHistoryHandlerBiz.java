package com.xunye.dispatch.biz;

import com.xunye.order.event.PolicyHistoryCreateEvent;

/**
 * @ClassName PolicyHistoryHandlerBiz
 * @Description
 * @Author boyiz
 * @Date 2023/5/2 23:18
 * @Version 1.0
 **/
public interface PolicyHistoryHandlerBiz {

    void onUserOrderCreatePolicyHistory(PolicyHistoryCreateEvent event);
}
