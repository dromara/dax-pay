package cn.bootx.platform.daxpay.core.pay.strategy;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayCode;
import cn.bootx.platform.daxpay.core.pay.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.pay.result.PaySyncResult;
import cn.bootx.platform.daxpay.core.payment.service.PaymentService;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPaymentManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayment;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayCancelService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPayService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPaySyncService;
import cn.bootx.platform.daxpay.core.channel.wechat.service.WeChatPaymentService;
import cn.bootx.platform.daxpay.exception.payment.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.bootx.platform.daxpay.param.channel.wechat.WeChatPayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付
 *
 * @author xxm
 * @date 2021/4/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPayStrategy extends AbsPayStrategy {

    private final WeChatPayConfigManager weChatPayConfigManager;

    private final WeChatPayService weChatPayService;

    private final WeChatPaymentService weChatPaymentService;

    private final WeChatPaymentManager weChatPaymentManager;

    private final WeChatPayCancelService weChatPayCancelService;

    private final WeChatPaySyncService weChatPaySyncService;

    private final PaymentService paymentService;

    private WeChatPayConfig weChatPayConfig;

    private WeChatPayParam weChatPayParam;

    /**
     * 类型
     */
    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 微信参数验证
            String extraParamsJson = this.getPayWayParam().getExtraParamsJson();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                this.weChatPayParam = JSONUtil.toBean(extraParamsJson, WeChatPayParam.class);
            }
            else {
                this.weChatPayParam = new WeChatPayParam();
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }

        // 检查金额
        PayWayParam payMode = this.getPayWayParam();
        if (BigDecimalUtil.compareTo(payMode.getAmount(), BigDecimal.ZERO) < 1) {
            throw new PayAmountAbnormalException();
        }

        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        weChatPayService.validation(this.getPayWayParam(), weChatPayConfig);
    }

    /**
     * 发起支付
     */
    @Override
    public void doPayHandler() {
        weChatPayService.pay(this.getPayWayParam().getAmount(), this.getPayment(), this.weChatPayParam, this.getPayWayParam(),
                this.weChatPayConfig);
    }

    /**
     * 支付调起成功
     */
    @Override
    public void doSuccessHandler() {
        weChatPaymentService.updatePaySuccess(this.getPayment(), this.getPayWayParam());
    }

    /**
     * 错误处理
     */
    @Override
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
        this.doCloseHandler();
    }

    /**
     * 异步支付成功
     */
    @Override
    public void doAsyncSuccessHandler(Map<String, String> map) {
        String tradeNo = map.get(WeChatPayCode.TRANSACTION_ID);
        weChatPaymentService.updateAsyncSuccess(this.getPayment().getId(), this.getPayWayParam(), tradeNo);
    }

    /**
     * 异步支付失败
     */
    @Override
    public void doAsyncErrorHandler(ExceptionInfo exceptionInfo) {
        // 调用撤销支付
        this.doCancelHandler();
    }

    /**
     * 撤销支付
     */
    @Override
    public void doCancelHandler() {
        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        weChatPayCancelService.cancelRemote(this.getPayment(), weChatPayConfig);
        // 调用关闭本地支付记录
        this.doCloseHandler();
    }

    /**
     * 关闭本地支付记录
     */
    @Override
    public void doCloseHandler() {
        weChatPaymentService.updateClose(this.getPayment().getId());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        this.initWeChatPayConfig();
        WeChatPayment weChatPayment = weChatPaymentManager.findByPaymentId(this.getPayment().getId())
            .orElseThrow(() -> new PayFailureException("微信支付记录不存在"));
        weChatPayCancelService.refund(this.getPayment(), weChatPayment, this.getPayWayParam().getAmount(),
                this.weChatPayConfig);
        weChatPaymentService.updatePayRefund(weChatPayment, this.getPayWayParam().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getPayWayParam().getAmount(), PayChannelEnum.WECHAT);
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResult doSyncPayStatusHandler() {
        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        return weChatPaySyncService.syncPayStatus(this.getPayment().getId(), this.weChatPayConfig);
    }

    /**
     * 初始化微信支付
     */
    private void initWeChatPayConfig() {
        // 检查并获取微信支付配置
        this.weChatPayConfig = Optional.ofNullable(this.weChatPayConfig)
            .orElse(weChatPayConfigManager.findActivity().orElseThrow(() -> new PayFailureException("支付配置不存在")));
    }

}
