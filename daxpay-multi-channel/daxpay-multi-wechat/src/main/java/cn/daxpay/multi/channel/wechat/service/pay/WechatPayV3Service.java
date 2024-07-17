package cn.daxpay.multi.channel.wechat.service.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.config.pay.WechatPayParam;
import cn.daxpay.multi.core.param.payment.pay.PayParam;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信支付服务v3版本
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayV3Service {

    /**
     * 支付前检查支付方式是否可用
     */
    public void validation(PayParam payParam) {

    }

    /**
     * 调起支付
     */
    public void pay(PayOrder payOrder, WechatPayParam wechatPayParam, WechatPayConfig config) {
        WxPayConfig payConfig = new WxPayConfig();
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
    }


}
