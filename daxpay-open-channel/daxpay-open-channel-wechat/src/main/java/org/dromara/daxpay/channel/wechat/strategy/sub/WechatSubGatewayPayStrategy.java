package org.dromara.daxpay.channel.wechat.strategy.sub;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.channel.wechat.param.pay.WechatPayParam;
import org.dromara.daxpay.channel.wechat.service.payment.config.WechatPayConfigService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.core.param.gateway.GatewayCashierPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.service.strategy.AbsGatewayPayStrategy;
import org.springframework.stereotype.Component;

/**
 * 微信服务商网关支付
 * @author xxm
 * @since 2025/3/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WechatSubGatewayPayStrategy extends AbsGatewayPayStrategy {

    private final WechatPayConfigService wechatPayConfigService;
    /**
     * 支付参数处理
     */
    @Override
    public void handlePayParam(GatewayCashierPayParam cashierPayParam, PayParam payParam) {
        var wechatPayConfig = wechatPayConfigService.getAndCheckConfig(true);
        var wechatPayParam = new WechatPayParam();
        wechatPayParam.setOpenIdType(wechatPayConfig.getAuthType());
        payParam.setExtraParam(JSONUtil.toJsonStr(wechatPayParam));
    }

    /**
     * 策略标识, 可以自行进行扩展
     *
     * @see ChannelEnum
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT_ISV.getCode();
    }
}
