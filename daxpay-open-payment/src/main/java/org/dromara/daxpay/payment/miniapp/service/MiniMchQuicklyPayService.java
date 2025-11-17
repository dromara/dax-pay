package org.dromara.daxpay.payment.miniapp.service;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import org.dromara.daxpay.payment.common.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayReadConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.MiniQuicklyConfigService;
import org.dromara.daxpay.payment.miniapp.param.quickly.QuicklyBarPayParam;
import org.dromara.daxpay.payment.miniapp.param.quickly.QuicklyQrPayParam;
import org.dromara.daxpay.payment.pay.local.PaymentContextLocal;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.unipay.enums.GatewayPayTypeEnum;
import org.dromara.daxpay.payment.unipay.param.gateway.AggregateBarPayParam;
import org.dromara.daxpay.payment.unipay.param.gateway.GatewayPayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import org.dromara.daxpay.payment.unipay.service.gateway.GatewayPayAssistService;
import org.dromara.daxpay.payment.unipay.service.gateway.GatewayPayService;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 小程序快捷支付
 * @author xxm
 * @since 2025/4/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MiniMchQuicklyPayService {
    private final GatewayPayService gatewayPayService;
    private final GatewayPayAssistService gatewayPayAssistService;
    private final PaymentAssistService paymentAssistService;
    private final MiniQuicklyConfigService quicklyPayController;
    private final GatewayPayReadConfigService gatewayPayReadConfigService;

    /**
     * 生成聚合支付码
     */
    public PayResult genAggregateUrl(QuicklyQrPayParam param){
        // 初始化商户和应用信息, 后续需要相关的环境
        paymentAssistService.initMchAndApp(param.getAppId());
        var reqInfo = PaymentContextLocal.get().getReqInfo();

        var readConfig = gatewayPayReadConfigService.findByAppId(reqInfo.getAppId());
        var quicklyConfig = quicklyPayController.getConfig(reqInfo.getAppId(), readConfig);
        var gatewayPayParam = new GatewayPayParam();
        gatewayPayParam.setBizOrderNo(TradeNoGenerateUtil.pay())
                .setTitle(StrUtil.format("小程序收银金额: {}元", param.getAmount()))
                .setGatewayPayType(GatewayPayTypeEnum.AGGREGATE.getCode())
                .setLimitPay(quicklyConfig.getLimitPay())
                .setAmount(param.getAmount())
                .setDescription(param.getDescription());
        gatewayPayParam.setMchNo(reqInfo.getMchNo());
        gatewayPayParam.setAppId(reqInfo.getAppId());
        gatewayPayParam.setReqTime(LocalDateTime.now());
        // 设置IP
        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("127.0.0.1");
        gatewayPayParam.setClientIp(ip);

        // 执行支付前的保存动作, 保存支付订单和扩展记录
        var payOrder = gatewayPayAssistService.createPayOrder(gatewayPayParam);
        // 生成
        String gatewayUrl = gatewayPayService.getGatewayUrl(payOrder.getOrderNo(), GatewayPayTypeEnum.AGGREGATE.getCode());
        return new PayResult()
                .setPayBody(gatewayUrl)
                .setOrderNo(payOrder.getOrderNo());
    }

    /**
     * 被扫支付
     */
    public PayResult barPay(QuicklyBarPayParam param){
        // 初始化商户和应用信息, 后续需要相关的环境
        paymentAssistService.initMchAndApp(param.getAppId());
        var reqInfo = PaymentContextLocal.get().getReqInfo();
        var readConfig = gatewayPayReadConfigService.findByAppId(reqInfo.getAppId());
        var quicklyConfig = quicklyPayController.getConfig(reqInfo.getAppId(), readConfig);
        var barPayParam = new AggregateBarPayParam().setBizOrderNo(TradeNoGenerateUtil.pay())
                .setAuthCode(param.getAuthCode())
                .setTitle(StrUtil.format("小程序收银金额: {}元", param.getAmount()))
                .setDescription(param.getDescription())
                .setTerminalNo(quicklyConfig.getTerminalNo())
                .setLimitPay(quicklyConfig.getLimitPay())
                .setAmount(param.getAmount());

        barPayParam.setMchNo(reqInfo.getMchNo());
        barPayParam.setAppId(reqInfo.getAppId());
        barPayParam.setReqTime(LocalDateTime.now());
        return gatewayPayService.aggregateBarPay(barPayParam);
    }
}
