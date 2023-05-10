package com.xunye.order.event;

import java.io.Serializable;
import java.util.List;

import com.xunye.auth.entity.User;
import com.xunye.order.entity.OrderInfo;
import com.xunye.promotion.dto.CouponsEditDTO;
import com.xunye.promotion.dto.CouponsHistoryEditDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName CouponsHistoryUpdateEvent
 * @Description 订单优惠券使用记录修改事件
 * @Author boyiz
 * @Date 2023/5/3 13:39
 * @Version 1.0
 **/

@Getter
@Setter
@ToString
public class CouponsHistoryUpdateEvent extends ApplicationEvent implements Serializable {
    private OrderInfo orderInfo;
    private List<CouponsHistoryEditDTO> couponsHistoryCollect;
    private User operatorUser;

    public CouponsHistoryUpdateEvent(Object source, OrderInfo orderInfo, List<CouponsHistoryEditDTO> couponsHistoryCollect, User operatorUser) {
        super(source);
        this.orderInfo = orderInfo;
        this.couponsHistoryCollect = couponsHistoryCollect;
        this.operatorUser = operatorUser;
    }
}
