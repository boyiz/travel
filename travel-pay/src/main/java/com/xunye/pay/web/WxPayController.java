package com.xunye.pay.web;

import com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.partnerpayments.jsapi.model.PrepayWithRequestPaymentResponse;
import com.xunye.core.exception.BusinessException;
import com.xunye.core.result.R;
import com.xunye.core.tools.CheckTools;
import com.xunye.order.dto.OrderInfoEditDTO;
import com.xunye.order.em.OrderStatusEnum;
import com.xunye.order.service.IOrderInfoService;
import com.xunye.pay.config.WxPayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PayWebController
 * @Description 微信支付Controller
 * @Author boyiz
 * @Date 2023/5/4 16:14
 * @Version 1.0
 **/

@RestController
@RequestMapping("/wxpay")
public class WxPayController {

    @Autowired
    private IOrderInfoService orderService;

    @Autowired
    private WxPayConfig payConfig;

    /**
     * 微信支付
     */
    @GetMapping(value = "/{orderSn}")
    public R<PrepayWithRequestPaymentResponse> wxPayOrder(@PathVariable("orderSn") String orderSn) {

        OrderInfoEditDTO orderInfoEditDTO = orderService.queryOrderInfoByOrderSn(orderSn);
        if (CheckTools.isNullOrEmpty(orderInfoEditDTO)) {
            throw new BusinessException("支付失败：订单不存在");
        }
        if (orderInfoEditDTO.getPayType().equals(OrderStatusEnum.FINISH.getValue())) {
            throw new BusinessException("支付失败：订单已付款");
        }
        if (orderInfoEditDTO.getPayType().equals(OrderStatusEnum.CLOSED.getValue())) {
            throw new BusinessException("支付失败：订单已关闭，请重新下单");
        }
        if (orderInfoEditDTO.getPayType().equals(OrderStatusEnum.INVALID.getValue())) {
            throw new BusinessException("支付失败：订单无效，请重新下单");
        }

        // 商户申请的公众号对应的appid，由微信支付生成，可在公众号后台查看
        String requestPaymentAppid = "test-request-payment-appid" ;
        PrepayRequest request = new PrepayRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        request.setSpAppid("test-sp-appid");
        // 调用接口
        PrepayWithRequestPaymentResponse prepayWithRequestPaymentResponse = payConfig.initWxPayConfig()
            .prepayWithRequestPayment(request, requestPaymentAppid);

        //TODO Order监听器
        return R.success("success", prepayWithRequestPaymentResponse);
    }



}
