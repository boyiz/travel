package com.xunye.dispatch.listener;

import com.xunye.dispatch.biz.GroupStockHandlerBiz;
import com.xunye.dispatch.biz.PolicyHistoryHandlerBiz;
import com.xunye.order.event.GroupStockUpdateEvent;
import com.xunye.order.event.PolicyHistoryCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @ClassName GroupStockUpdateEventListener
 * @Description
 * @Author boyiz
 * @Date 2023/5/9 11:35
 * @Version 1.0
 **/
@Slf4j
@Component
public class GroupStockEventListener {

    @Autowired
    private GroupStockHandlerBiz groupStockHandlerBiz;

    @TransactionalEventListener(classes = {GroupStockUpdateEvent.class}, phase = TransactionPhase.BEFORE_COMMIT)
    public void userGroupStock(GroupStockUpdateEvent event){
        log.info("【调度中心】监听到【团库存更新事件】event，event={}", event);
        groupStockHandlerBiz.onGroupStockUpdate(event);
    }
}
