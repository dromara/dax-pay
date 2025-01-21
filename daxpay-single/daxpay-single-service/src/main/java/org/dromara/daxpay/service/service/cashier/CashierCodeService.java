package org.dromara.daxpay.service.service.cashier;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.util.ValidationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.cashier.CashierCodeAuthCodeParam;
import org.dromara.daxpay.core.param.cashier.CashierCodeAuthUrlParam;
import org.dromara.daxpay.core.param.cashier.CashierCodePayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.cashier.CashierCodeConfigService;
import org.dromara.daxpay.service.service.trade.pay.PayService;
import org.dromara.daxpay.service.strategy.AbsCashierCodeStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 收银码牌服务
 * @author xxm
 * @since 2024/9/28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodeService {

    private final CashierCodeConfigService codeConfigService;

    private final PaymentAssistService paymentAssistService;

    private final PayService payService;

    /**
     * 生成授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    public String generateAuthUrl(CashierCodeAuthUrlParam param){
        // 查询配置
        var cashierConfig = codeConfigService.findByCashierType(param.getCashierCode(),param.getCashierType());
        paymentAssistService.initMchApp(cashierConfig.getAppId());
        // 获取策略
        AbsCashierCodeStrategy cashierStrategy = PaymentStrategyFactory.create(cashierConfig.getChannel(), AbsCashierCodeStrategy.class);
        return cashierStrategy.generateAuthUrl(param.getCashierCode());
    }

    /**
     * 授权结果
     */
    public AuthResult auth(CashierCodeAuthCodeParam param) {
        // 查询配置
        var cashierConfig = codeConfigService.findByCashierType(param.getCashierCode(),param.getCashierType());
        paymentAssistService.initMchApp(cashierConfig.getAppId());
        // 获取策略
        AbsCashierCodeStrategy cashierStrategy = PaymentStrategyFactory.create(cashierConfig.getChannel(), AbsCashierCodeStrategy.class);
        AuthCodeParam authCodeParam = new AuthCodeParam()
                .setAuthCode(param.getAuthCode());
        return cashierStrategy.doAuth(authCodeParam);
    }

    /**
     * 支付处理
     */
    public PayResult cashierPay(CashierCodePayParam param){
        String clientIP = JakartaServletUtil.getClientIP(WebServletUtil.getRequest());
        // 查询配置
        var cashierConfig = codeConfigService.findByCashierType(param.getCashierCode(), param.getCashierType());
        paymentAssistService.initMchApp(cashierConfig.getAppId());
        // 构建支付参数
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo(TradeNoGenerateUtil.pay());
        payParam.setTitle(StrUtil.format("手机收银金额: {}元", param.getAmount()));
        payParam.setDescription(param.getDescription());
        payParam.setChannel(cashierConfig.getChannel());
        payParam.setMethod(cashierConfig.getPayMethod());
        payParam.setAppId(cashierConfig.getAppId());
        payParam.setAmount(param.getAmount());
        payParam.setClientIp(clientIP);
        payParam.setReqTime(LocalDateTime.now());
        String sign = paymentAssistService.genSign(payParam);
        payParam.setSign(sign);

        // 获取策略
        AbsCashierCodeStrategy cashierStrategy = PaymentStrategyFactory.create(cashierConfig.getChannel(), AbsCashierCodeStrategy.class);
        // 进行参数预处理
        cashierStrategy.handlePayParam(param, payParam);
        // 参数校验
        ValidationUtil.validateParam(payParam);
        // 发起支付
        return payService.pay(payParam);
    }
}
