package com.xunye.order.notify;

import java.util.List;

import javax.annotation.Resource;

import com.xunye.auth.entity.User;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.event.CouponsHistoryUpdateEvent;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @ClassName CouponsHistoryNotifier
 * @Description TODO
 * @Author boyiz
 * @Date 2023/5/3 13:36
 * @Version 1.0
 **/

@Component
public class CouponsHistoryNotifier {

    @Resource
    private ApplicationContext applicationContext;

    public void updateCouponsHistory(OrderInfo orderInfoEntity,
        List<CouponsHistoryEditDTO> couponsHistoryCollect, User operatorUser) {
        applicationContext.publishEvent(new CouponsHistoryUpdateEvent(this, orderInfoEntity,couponsHistoryCollect,operatorUser));

    }
}
