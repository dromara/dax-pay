package cn.daxpay.multi.channel.wechat.service.pay;

import cn.daxpay.multi.channel.wechat.entity.config.WechatPayConfig;
import cn.daxpay.multi.channel.wechat.param.pay.WechatPayParam;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 微信支付服务v2版本
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayV2Service {

    public void pay(PayOrder order, WechatPayParam wechatPayParam, WechatPayConfig wechatPayConfig) {

    }
}
