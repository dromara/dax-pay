package cn.daxpay.single.service.core.payment.pay.strategy;

import cn.daxpay.single.core.code.PayChannelEnum;
import cn.bootx.platform.common.core.exception.ValidationFailedException;
import cn.daxpay.single.core.param.channel.AliPayParam;
import cn.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import cn.daxpay.single.service.core.channel.alipay.service.AliPayService;
import cn.daxpay.single.service.func.AbsPayStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付
 * @author xxm
 * @since 2021/2/27
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayStrategy extends AbsPayStrategy {

    private final AliPayService aliPayService;

    private final AliPayConfigService alipayConfigService;

    private AliPayConfig alipayConfig;

    private AliPayParam aliPayParam;

     @Override
    public String getChannel() {
        return PayChannelEnum.ALI.getCode();
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            Map<String, Object> channelParam = this.getPayParam().getExtraParam();
            if (CollUtil.isNotEmpty(channelParam)) {
                this.aliPayParam = BeanUtil.toBean(channelParam, AliPayParam.class);
            }
            else {
                this.aliPayParam = new AliPayParam();
            }
        }
        catch (JSONException e) {
            throw new ValidationFailedException("支付参数错误");
        }
        // 检查并获取支付宝支付配置
        this.alipayConfig = alipayConfigService.getAndCheckConfig();
        // 支付宝相关校验
        aliPayService.validation(this.getPayParam(), alipayConfig);
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        aliPayService.pay(this.getOrder(), this.aliPayParam, this.alipayConfig);
    }

}
