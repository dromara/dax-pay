package cn.bootx.platform.daxpay.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.service.common.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.service.core.channel.alipay.entity.AliPayConfig;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayCloseService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayConfigService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayOrderService;
import cn.bootx.platform.daxpay.service.core.channel.alipay.service.AliPayService;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付
 *
 * @author xxm
 * @since 2021/2/27
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayStrategy extends AbsPayStrategy {

    private final AliPayOrderService aliPaymentService;

    private final AliPayService aliPayService;

    private final AliPayConfigService alipayConfigService;

    private final AliPayCloseService aliPayCancelService;

    private AliPayConfig alipayConfig;

    private AliPayParam aliPayParam;

    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.ALI;
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            String extraParamsJson = this.getPayWayParam().getChannelExtra();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                this.aliPayParam = JSONUtil.toBean(extraParamsJson, AliPayParam.class);
            }
            else {
                this.aliPayParam = new AliPayParam();
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }
        // 检查金额
        PayWayParam payMode = this.getPayWayParam();
        if (payMode.getAmount() <= 0) {
            throw new PayAmountAbnormalException();
        }
        // 检查并获取支付宝支付配置
        this.initAlipayConfig();
        aliPayService.validation(this.getPayWayParam(), alipayConfig);
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        aliPayService.pay( this.getOrder(), this.getPayWayParam(), this.aliPayParam, this.alipayConfig);
    }

    /**
     * 支付调起成功
     */
    @Override
    public void doSuccessHandler() {
        aliPaymentService.updatePaySuccess(this.getOrder(), this.getPayWayParam());
    }

    /**
     * 发起支付失败
     */
    @Override
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
        this.doCloseHandler();
    }

    /**
     * 关闭支付记录
     */
    @Override
    public void doCloseHandler() {
        // 关闭支付
        aliPayCancelService.close(this.getOrder());
        aliPaymentService.updateClose(this.getOrder().getId());
    }


    /**
     * 初始化支付宝配置信息
     */
    private void initAlipayConfig() {
        // 获取并初始化支付宝支付配置
        this.alipayConfig = alipayConfigService.getConfig();
        alipayConfigService.initConfig(this.alipayConfig);
    }

}
