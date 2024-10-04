package org.dromara.daxpay.service.service.cashier;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.util.ValidationUtil;
import org.dromara.daxpay.core.param.cashier.CashierAuthCodeParam;
import org.dromara.daxpay.core.param.cashier.CashierAuthUrlParam;
import org.dromara.daxpay.core.param.cashier.CashierPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.ChannelCashierConfigService;
import org.dromara.daxpay.service.service.trade.pay.PayService;
import org.dromara.daxpay.service.strategy.AbsChannelCashierStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 通知支付控制台服务
 * @author xxm
 * @since 2024/9/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelCashierService {

    private final ChannelCashierConfigService channelCashierConfigService;

    private final PaymentAssistService paymentAssistService;

    private final PayService payService;

    /**
     * 生成授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    public String generateAuthUrl(CashierAuthUrlParam param){
        // 查询配置
        var cashierConfig = channelCashierConfigService.findByCashierType(param.getCashierType());
        // 获取策略
        AbsChannelCashierStrategy cashierStrategy = PaymentStrategyFactory.create(cashierConfig.getChannel(), AbsChannelCashierStrategy.class);
        return cashierStrategy.generateAuthUrl(param);
    }


    /**
     * 授权结果
     */
    public AuthResult auth(CashierAuthCodeParam param) {
        // 查询配置
        var cashierConfig = channelCashierConfigService.findByCashierType(param.getCashierType());
        // 获取策略
        AbsChannelCashierStrategy cashierStrategy = PaymentStrategyFactory.create(cashierConfig.getChannel(), AbsChannelCashierStrategy.class);
        return cashierStrategy.doAuth(param);
    }

    /**
     * 支付处理
     */
    public PayResult cashierPay(CashierPayParam param){
        String clientIP = JakartaServletUtil.getClientIP(WebServletUtil.getRequest());
        // 查询配置
        var cashierConfig = channelCashierConfigService.findByCashierType(param.getCashierType());
        // 构建支付参数
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo(TradeNoGenerateUtil.pay());
        payParam.setTitle(StrUtil.format("手机收银金额: {}元", param.getAmount()));
        payParam.setDescription(param.getDescription());
        payParam.setChannel(cashierConfig.getChannel());
        payParam.setMethod(cashierConfig.getPayMethod());
        payParam.setAmount(param.getAmount());
        payParam.setAppId(param.getAppId());
        payParam.setClientIp(clientIP);
        payParam.setReqTime(LocalDateTime.now());
        String sign = paymentAssistService.genSign(payParam);
        payParam.setSign(sign);

        // 获取策略
        AbsChannelCashierStrategy cashierStrategy = PaymentStrategyFactory.create(cashierConfig.getChannel(), AbsChannelCashierStrategy.class);
        // 进行参数预处理
        cashierStrategy.handlePayParam(param, payParam);
        // 参数校验
        ValidationUtil.validateParam(payParam);
        // 发起支付
        return payService.pay(payParam);
    }

}
