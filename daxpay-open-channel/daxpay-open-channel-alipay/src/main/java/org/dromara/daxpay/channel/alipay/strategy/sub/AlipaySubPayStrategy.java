package org.dromara.daxpay.channel.alipay.strategy.sub;

import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.core.exception.ValidationFailedException;
import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import org.dromara.daxpay.channel.alipay.param.pay.AlipayParam;
import org.dromara.daxpay.channel.alipay.service.payment.config.AlipayConfigService;
import org.dromara.daxpay.channel.alipay.service.payment.pay.AliPayService;
import org.dromara.daxpay.core.enums.ChannelEnum;
import org.dromara.daxpay.service.pay.bo.trade.PayResultBo;
import org.dromara.daxpay.service.pay.strategy.AbsPayStrategy;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付
 * @author xxm
 * @since 2021/2/27
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AlipaySubPayStrategy extends AbsPayStrategy {

    private final AliPayService aliPayService;

    private final AlipayConfigService aliPayConfigService;

    private AlipayParam aliPayParam;

    private AliPayConfig aliPayConfig;

    @Override
    public String getChannel() {
        return ChannelEnum.ALIPAY_ISV.getCode();
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            String channelParam = this.getPayParam().getExtraParam();
            if (StrUtil.isNotBlank(channelParam)) {
                this.aliPayParam = JacksonUtil.toBean(channelParam, AlipayParam.class);
            }
            else {
                this.aliPayParam = new AlipayParam();
            }
        }
        catch (JSONException e) {
            throw new ValidationFailedException("支付参数错误");
        }
        aliPayConfig = aliPayConfigService.getAndCheckConfig(true);
        // 支付宝相关校验
        aliPayService.validation(this.getPayParam(),aliPayConfig);
    }

    /**
     * 发起支付操作
     */
    @Override
    public PayResultBo doPayHandler() {
        return aliPayService.pay(this.getOrder(), this.getPayParam(), this.aliPayParam,aliPayConfig);
    }
}
