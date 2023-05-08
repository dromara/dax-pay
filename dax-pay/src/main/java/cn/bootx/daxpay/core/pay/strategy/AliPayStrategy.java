package cn.bootx.daxpay.core.pay.strategy;

import cn.bootx.common.core.exception.BizException;
import cn.bootx.common.core.util.BigDecimalUtil;
import cn.bootx.daxpay.code.pay.PayChannelCode;
import cn.bootx.daxpay.code.pay.PayChannelEnum;
import cn.bootx.daxpay.code.paymodel.AliPayCode;
import cn.bootx.daxpay.core.pay.exception.ExceptionInfo;
import cn.bootx.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.daxpay.core.pay.result.PaySyncResult;
import cn.bootx.daxpay.core.payment.service.PaymentService;
import cn.bootx.daxpay.core.paymodel.alipay.dao.AlipayConfigManager;
import cn.bootx.daxpay.core.paymodel.alipay.entity.AlipayConfig;
import cn.bootx.daxpay.core.paymodel.alipay.service.AliPayCancelService;
import cn.bootx.daxpay.core.paymodel.alipay.service.AliPayService;
import cn.bootx.daxpay.core.paymodel.alipay.service.AliPaymentService;
import cn.bootx.daxpay.core.paymodel.alipay.service.AlipaySyncService;
import cn.bootx.daxpay.exception.payment.PayAmountAbnormalException;
import cn.bootx.daxpay.exception.payment.PayFailureException;
import cn.bootx.daxpay.param.pay.PayModeParam;
import cn.bootx.daxpay.param.paymodel.alipay.AliPayParam;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付
 *
 * @author xxm
 * @date 2021/2/27
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayStrategy extends AbsPayStrategy {

    private final AliPaymentService aliPaymentService;

    private final AlipaySyncService alipaySyncService;

    private final AliPayService aliPayService;

    private final AliPayCancelService aliPayCancelService;

    private final AlipayConfigManager alipayConfigManager;

    private final PaymentService paymentService;

    private AlipayConfig alipayConfig;

    private AliPayParam aliPayParam;

    @Override
    public int getType() {
        return PayChannelCode.ALI;
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            String extraParamsJson = this.getPayMode().getExtraParamsJson();
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
        PayModeParam payMode = this.getPayMode();
        if (BigDecimalUtil.compareTo(payMode.getAmount(), BigDecimal.ZERO) < 1) {
            throw new PayAmountAbnormalException();
        }
        // 检查并获取支付宝支付配置
        this.initAlipayConfig();
        aliPayService.validation(this.getPayMode(), alipayConfig);
        // 如果没有显式传入同步回调地址, 使用默认配置
        if (StrUtil.isBlank(aliPayParam.getReturnUrl())) {
            aliPayParam.setReturnUrl(alipayConfig.getReturnUrl());
        }
        this.initAlipayConfig();
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        aliPayService.pay(this.getPayMode().getAmount(), this.getPayment(), this.aliPayParam, this.getPayMode(),
                this.alipayConfig);
    }

    /**
     * 支付调起成功
     */
    @Override
    public void doSuccessHandler() {
        aliPaymentService.updatePaySuccess(this.getPayment(), this.getPayMode());
    }

    /**
     * 发起支付失败
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
        String tradeNo = map.get(AliPayCode.TRADE_NO);
        aliPaymentService.updateAsyncSuccess(this.getPayment().getId(), this.getPayMode(), tradeNo);
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
        this.initAlipayConfig();
        // 撤销支付
        aliPayCancelService.cancelRemote(this.getPayment());
        // 调用关闭本地支付记录
        this.doCloseHandler();
    }

    /**
     * 关闭本地支付记录
     */
    @Override
    public void doCloseHandler() {
        aliPaymentService.updateClose(this.getPayment().getId());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        this.initAlipayConfig();
        aliPayCancelService.refund(this.getPayment(), this.getPayMode().getAmount());
        aliPaymentService.updatePayRefund(this.getPayment().getId(), this.getPayMode().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(), this.getPayMode().getAmount(), PayChannelEnum.ALI);
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResult doSyncPayStatusHandler() {
        this.initAlipayConfig();
        return alipaySyncService.syncPayStatus(this.getPayment());
    }

    /**
     * 初始化支付宝配置信息
     */
    private void initAlipayConfig() {
        // 检查并获取支付宝支付配置
        this.alipayConfig = alipayConfigManager.findActivity().orElseThrow(() -> new PayFailureException("支付配置不存在"));
        this.initApiConfig(this.alipayConfig);
    }

    /**
     * 初始化IJPay 服务
     */
    @SneakyThrows
    private void initApiConfig(AlipayConfig alipayConfig) {

        AliPayApiConfig aliPayApiConfig;
        // 公钥
        if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_KEY)) {
            aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(alipayConfig.getAppId())
                .setPrivateKey(alipayConfig.getPrivateKey())
                .setAliPayPublicKey(alipayConfig.getAlipayPublicKey())
                .setCharset(CharsetUtil.UTF_8)
                .setServiceUrl(alipayConfig.getServerUrl())
                .setSignType(alipayConfig.getSignType())
                .build();
        }
        // 证书
        else if (Objects.equals(alipayConfig.getAuthType(), AliPayCode.AUTH_TYPE_CART)) {
            aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(alipayConfig.getAppId())
                .setPrivateKey(alipayConfig.getPrivateKey())
                .setAppCertContent(alipayConfig.getAppCert())
                .setAliPayCertContent(alipayConfig.getAlipayCert())
                .setAliPayRootCertContent(alipayConfig.getAlipayRootCert())
                .setCharset(CharsetUtil.UTF_8)
                .setServiceUrl(alipayConfig.getServerUrl())
                .setSignType(alipayConfig.getSignType())
                .buildByCertContent();
        }
        else {
            throw new BizException("支付宝认证方式不可为空");
        }
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }

}
