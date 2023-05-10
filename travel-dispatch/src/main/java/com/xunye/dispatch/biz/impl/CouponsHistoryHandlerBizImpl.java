package com.xunye.dispatch.biz.impl;

import java.util.Date;
import java.util.List;

import com.xunye.auth.entity.User;
import com.xunye.dispatch.biz.CouponsHistoryHandlerBiz;
import com.xunye.order.entity.OrderInfo;
import com.xunye.order.event.CouponsHistoryUpdateEvent;
import com.xunye.promotion.dto.CouponsEditDTO;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import com.xunye.promotion.em.CouponsUseStatusEnum;
import com.xunye.promotion.service.ICouponsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CouponsHistoryHandlerBizImpl
 * @Description
 * @Author boyiz
 * @Date 2023/5/3 13:42
 * @Version 1.0
 **/
@Service
public class CouponsHistoryHandlerBizImpl implements CouponsHistoryHandlerBiz {

    @Autowired
    private ICouponsHistoryService couponsHistoryService;


    @Override
    public void onUserOrderUpdateCouponsHistory(CouponsHistoryUpdateEvent event) {
        User operatorUser = event.getOperatorUser();
        List<CouponsHistoryEditDTO> couponsCollect = event.getCouponsHistoryCollect();
        OrderInfo orderInfo = event.getOrderInfo();
        for (CouponsHistoryEditDTO couponsHistoryEditDTO : couponsCollect) {
            couponsHistoryEditDTO.setId(couponsHistoryEditDTO.getId());
            couponsHistoryEditDTO.setOrderId(orderInfo.getId());
            couponsHistoryEditDTO.setOrderSn(orderInfo.getOrderSn());
            couponsHistoryEditDTO.setUseStatus(CouponsUseStatusEnum.ALREADY_USE.getValue());
            couponsHistoryEditDTO.setUseTime(new Date());
            couponsHistoryService.updateCouponsHistory(couponsHistoryEditDTO,operatorUser);
        }
    }
}
