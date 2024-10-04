package org.dromara.daxpay.channel.wechat.service.close;

import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.clode.WxPayOrderReverseV3Request;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.core.exception.TradeFailException;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信支付关闭 V3
 * @author xxm
 * @since 2024/7/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayCloseV3Service {
    private final WechatPayConfigService wechatPayConfigService;
    private static final Gson GSON = new GsonBuilder().create();

    /**
     * 关闭支付, 微信对已经关闭的支付单也可以重复关闭
     */
    public void close(PayOrder payOrder, WechatPayConfig weChatPayConfig) {
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(weChatPayConfig);
        try {
            wxPayService.closeOrderV3(payOrder.getOrderNo());
        } catch (WxPayException e) {
            log.error("微信关闭支付V3失败", e);
            throw new TradeFailException("微信退款V3失败: "+e.getMessage());
        }
    }

    /**
     * 撤销支付
     */
    public void cancel(PayOrder payOrder, WechatPayConfig config){
        WxPayService wxPayService = wechatPayConfigService.wxJavaSdk(config);
        try {
            WxPayConfig wxPayConfig = wxPayService.getConfig();
            // 设置公共属性
            WxPayOrderReverseV3Request request = new WxPayOrderReverseV3Request();
            request.setMchid(wxPayConfig.getMchId());
            request.setAppid(wxPayConfig.getAppId());

            // 拼接参数请求路径并发送
            String url = String.format("%s/v3/pay/transactions/out-trade-no/%s/reverse",
                    wxPayService.getPayBaseUrl(), payOrder.getOrderNo());
            wxPayService.postV3(url, GSON.toJson(request));
        } catch (WxPayException e) {
            log.error("微信撤销支付V3失败", e);
            throw new TradeFailException("微信撤销支付V3失败: "+e.getMessage());
        }
    }
}
