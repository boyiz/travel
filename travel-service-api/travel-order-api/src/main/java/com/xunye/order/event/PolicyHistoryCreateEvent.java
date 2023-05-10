package com.xunye.order.event;

import java.io.Serializable;
import java.util.List;

import com.xunye.auth.entity.User;
import com.xunye.order.entity.OrderInfo;
import com.xunye.promotion.dto.PolicyInfoEditDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName PolicyHistoryCreateEvent
 * @Description 订单使用优惠记录创建事件
 * @Author boyiz
 * @Date 2023/5/2 20:24
 * @Version 1.0
 **/

@Getter
@Setter
@ToString
public class PolicyHistoryCreateEvent extends ApplicationEvent implements Serializable {

    private OrderInfo orderInfo;
    private List<PolicyInfoEditDTO> policyCollect;
    private User operatorUser;

    public PolicyHistoryCreateEvent(Object source, OrderInfo orderInfo,List<PolicyInfoEditDTO> policyCollect, User operatorUser) {
        super(source);
        this.orderInfo = orderInfo;
        this.policyCollect = policyCollect;
        this.operatorUser = operatorUser;
    }

}
