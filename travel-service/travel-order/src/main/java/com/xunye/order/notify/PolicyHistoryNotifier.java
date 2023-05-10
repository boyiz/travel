package com.xunye.order.notify;

import java.util.List;

import javax.annotation.Resource;

import com.xunye.auth.entity.User;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.event.PolicyHistoryCreateEvent;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @ClassName PolicyHistoryNotifier
 * @Description PolicyHistory 的 Event
 * @Author boyiz
 * @Date 2023/5/2 18:54
 * @Version 1.0
 **/

@Component
public class PolicyHistoryNotifier {
    @Resource
    private ApplicationContext applicationContext;

    public void createPolicyHistory(OrderInfo orderInfoEntity,
        List<PolicyInfoEditDTO> policyCollect, User operatorUser) {
        applicationContext.publishEvent(new PolicyHistoryCreateEvent(this, orderInfoEntity,policyCollect,operatorUser));

    }
}
