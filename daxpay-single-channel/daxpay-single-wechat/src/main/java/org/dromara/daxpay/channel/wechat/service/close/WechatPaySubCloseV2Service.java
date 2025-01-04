package org.dromara.daxpay.channel.wechat.service.close;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import com.github.binarywang.wxpay.bean.request.WxPayOrderReverseRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信支付关闭 V2
 * @author xxm
 * @since 2024/7/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPaySubCloseV2Service {
    private final WechatPayConfigService wechatPayConfigService;


    /**
     * 关闭支付, 微信对已经关闭的支付单也可以重复关闭
     */
    public void close(PayOrder payOrder, WechatPayConfig weChatPayConfig) {
            WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(weChatPayConfig);
            try {
                wxPayService.closeOrder(payOrder.getOrderNo());
            } catch (WxPayException e) {
                log.error("微信关闭订单V2失败", e);
                throw new TradeFailException("微信关闭订单V2失败: "+e.getMessage());
            }
    }


    /**
     * 撤销接口
     */
    public void cancel(PayOrder payOrder, WechatPayConfig weChatPayConfig){
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(weChatPayConfig);
        try {
            WxPayOrderReverseRequest request = new WxPayOrderReverseRequest();
            request.setOutTradeNo(payOrder.getOrderNo());
            wxPayService.reverseOrder(request);
        } catch (WxPayException e) {
            log.error("微信撤销订单V2失败", e);
            throw new TradeFailException("微信撤销订单V2失败: "+e.getMessage());
        }
    }

}
