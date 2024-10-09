package org.dromara.daxpay.channel.wechat.strategy;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import org.dromara.daxpay.channel.wechat.code.WechatPayCode;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import org.dromara.daxpay.channel.wechat.param.pay.WechatPayParam;
import org.dromara.daxpay.channel.wechat.service.config.WechatPayConfigService;
import org.dromara.daxpay.channel.wechat.service.pay.WechatPayService;
import org.dromara.daxpay.channel.wechat.service.pay.WechatPayV2Service;
import org.dromara.daxpay.channel.wechat.service.pay.WechatPayV3Service;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.bo.trade.PayResultBo;
import org.dromara.daxpay.service.strategy.AbsPayStrategy;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付
 * @author xxm
 * @since 2021/4/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WechatPayStrategy extends AbsPayStrategy {

    private final WechatPayConfigService aliPayConfigService;

    private final WechatPayService wechatPayService;

    private final WechatPayV2Service wechatPayV2Service;

    private final WechatPayV3Service wechatPayV3Service;

    private WechatPayConfig wechatPayConfig;

    private WechatPayParam wechatPayParam;

    /**
     * 策略标识, 可以自行进行扩展
     */
    @Override
    public String getChannel() {
        return ChannelEnum.WECHAT.getCode();
    }

    @Override
    public void doBeforePayHandler(){
        // 微信参数验证
        String channelParam = this.getPayParam().getExtraParam();
        if (StrUtil.isNotEmpty(channelParam)) {
            this.wechatPayParam = JacksonUtil.toBean(channelParam, WechatPayParam.class);
        } else {
            this.wechatPayParam = new WechatPayParam();
        }
        this.wechatPayConfig = aliPayConfigService.getAndCheckConfig();
        wechatPayService.validation(this.getPayParam(), this.wechatPayParam, wechatPayConfig);
    }

    /**
     * 支付操作
     */
    @Override
    public PayResultBo doPayHandler() {
        if (Objects.equals(wechatPayConfig.getApiVersion(), WechatPayCode.API_V2)){
            return wechatPayV2Service.pay(getOrder(), wechatPayParam, wechatPayConfig);
        } else {
            return wechatPayV3Service.pay(getOrder(), wechatPayParam, wechatPayConfig);
        }
    }
}
