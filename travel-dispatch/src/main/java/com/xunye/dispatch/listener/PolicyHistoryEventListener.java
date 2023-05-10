package com.xunye.dispatch.listener;

import com.xunye.dispatch.biz.PolicyHistoryHandlerBiz;
import com.xunye.order.event.PolicyHistoryCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @ClassName PolicyHistoryEventListener
 * @Description
 * @Author boyiz
 * @Date 2023/5/2 23:16
 * @Version 1.0
 **/

@Slf4j
@Component
public class PolicyHistoryEventListener {

    @Autowired
    private PolicyHistoryHandlerBiz policyHistoryHandlerBiz;

    @TransactionalEventListener(classes = {PolicyHistoryCreateEvent.class}, phase = TransactionPhase.BEFORE_COMMIT)
    public void userOrderCreatePolicyHistory(PolicyHistoryCreateEvent event){
        log.info("【调度中心】监听到【活动政策使用记录创建】event，event={}", event);
        policyHistoryHandlerBiz.onUserOrderCreatePolicyHistory(event);
    }

}
