package com.xunye.dispatch.listener;

import com.xunye.dispatch.biz.CouponsHistoryHandlerBiz;
import com.xunye.dispatch.biz.PolicyHistoryHandlerBiz;
import com.xunye.order.event.CouponsHistoryUpdateEvent;
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
public class CouponsHistoryEventListener {

    @Autowired
    private CouponsHistoryHandlerBiz couponsHistoryHandlerBiz;

    @TransactionalEventListener(classes = {CouponsHistoryUpdateEvent.class}, phase = TransactionPhase.BEFORE_COMMIT)
    public void userOrderUpdateCouponsHistory(CouponsHistoryUpdateEvent event){
        log.info("【调度中心】监听到【优惠券使用记录创建|使用】event，event={}", event);
        couponsHistoryHandlerBiz.onUserOrderUpdateCouponsHistory(event);
    }

}
