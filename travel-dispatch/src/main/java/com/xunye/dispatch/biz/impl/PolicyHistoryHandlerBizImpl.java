package com.xunye.dispatch.biz.impl;

import java.util.List;

import com.xunye.auth.entity.User;
import com.xunye.dispatch.biz.PolicyHistoryHandlerBiz;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.event.PolicyHistoryCreateEvent;
import com.xunye.promotion.dto.PolicyHistoryEditDTO;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import com.xunye.promotion.service.IPolicyHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName PolicyHistoryHandlerBizImpl
 * @Description
 * @Author boyiz
 * @Date 2023/5/2 23:18
 * @Version 1.0
 **/

@Service
public class PolicyHistoryHandlerBizImpl implements PolicyHistoryHandlerBiz {

    @Autowired
    private IPolicyHistoryService policyHistoryService;

    @Override
    public void onUserOrderCreatePolicyHistory(PolicyHistoryCreateEvent event) {
        User operatorUser = event.getOperatorUser();
        List<PolicyInfoEditDTO> policyCollect = event.getPolicyCollect();
        OrderInfo orderInfo = event.getOrderInfo();
        for (PolicyInfoEditDTO policyInfoEditDTO : policyCollect) {
            PolicyHistoryEditDTO policyHistoryEditDTO = new PolicyHistoryEditDTO();
            policyHistoryEditDTO.setPolicyId(policyInfoEditDTO.getId());
            policyHistoryEditDTO.setOrderId(orderInfo.getId());
            policyHistoryEditDTO.setOrderSn(orderInfo.getOrderSn());
            policyHistoryEditDTO.setUserId(operatorUser.getId());
            policyHistoryService.createPolicyHistory(policyHistoryEditDTO,operatorUser);
        }
    }
}
