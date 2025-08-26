package org.dromara.daxpay.service.pay.service.gateway;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.enums.CashierSceneEnum;
import org.dromara.daxpay.core.enums.ChannelAuthTypeEnum;
import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.param.gateway.GatewayCashierCodeAuthParam;
import org.dromara.daxpay.core.param.gateway.GatewayCashierCodeAuthUrlParam;
import org.dromara.daxpay.core.param.gateway.GatewayCashierCodePayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.device.dao.qrcode.config.CashierCodeConfigManager;
import org.dromara.daxpay.service.device.dao.qrcode.config.CashierCodeSceneConfigManager;
import org.dromara.daxpay.service.device.service.qrcode.CashierCodeService;
import org.dromara.daxpay.service.pay.result.gateway.GatewayCashierCodeConfigResult;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.pay.service.trade.pay.PayService;
import org.dromara.daxpay.service.pay.strategy.AbsChannelAuthStrategy;
import org.dromara.daxpay.service.pay.util.PaymentStrategyFactory;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 收银码牌支付
 * @author xxm
 * @since 2025/6/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodePayService {

    private final CashierCodeSceneConfigManager codeSceneConfigManager;
    private final CashierCodeConfigManager cashierCodeConfigManager;
    private final CashierCodeService cashierCodeService;
    private final PaymentAssistService paymentAssistService;
    private final PayService payService;

    /**
     * 收银码牌支付
     */
    @IgnoreTenant
    public PayResult cashierCodePay(GatewayCashierCodePayParam param){
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(param.getCashierCode());
        // 查询码牌配置
        var codeConfig = cashierCodeConfigManager.findById(cashierCode.getConfigId())
                .orElseThrow(() -> new DataNotExistException("码牌配置不存在"));
        var sceneConfig = codeSceneConfigManager.findByConfigAndScene(cashierCode.getConfigId(), param.getCashierScene())
                .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));

        paymentAssistService.initMchAndApp(cashierCode.getMchNo(), cashierCode.getAppId());
        // 构建支付参数
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo(TradeNoGenerateUtil.pay());

        payParam.setChannel(sceneConfig.getChannel());
        payParam.setMethod(sceneConfig.getPayMethod());
        payParam.setOtherMethod(sceneConfig.getOtherMethod());
        payParam.setTitle(StrUtil.format("{} 码牌收款: {}元", Optional.ofNullable(cashierCode.getName()).orElse(""), param.getAmount()));
        payParam.setLimitPay(codeConfig.getLimitPay());
        payParam.setDescription(param.getDescription());
        payParam.setAmount(param.getAmount());
        payParam.setOpenId(param.getOpenId());
        payParam.setReqTime(LocalDateTime.now());
        payParam.setAppId(cashierCode.getAppId());
        payParam.setMchNo(cashierCode.getMchNo());
        // 设置IP
        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("127.0.0.1");
        payParam.setClientIp(ip);

        // 发起支付
        return payService.pay(payParam);
    }

    /**
     * 根据码牌编码和类型查询信息和配置
     */
    @IgnoreTenant
    public GatewayCashierCodeConfigResult getCashierCodeConfig(String code, String scene) {
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(code);
        var sceneConfig = codeSceneConfigManager.findByConfigAndScene(cashierCode.getConfigId(), scene)
                .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));
        var result = new GatewayCashierCodeConfigResult();
        BeanUtil.copyProperties(sceneConfig, result);
        return result.setName(cashierCode.getName())
                .setAmountType(cashierCode.getAmountType())
                .setAmount(cashierCode.getAmount())
                .setEnable(cashierCode.getEnable());
    }

    /**
     * 生成码牌所需授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @IgnoreTenant
    public String genAuthUrl(GatewayCashierCodeAuthUrlParam param){
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(param.getCashierCode());
        // 查询码牌支付场景配置
        var sceneConfig = codeSceneConfigManager.findByConfigAndScene(cashierCode.getConfigId(), param.getCashierScene())
                .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));
        // 初始化上下文信息
        paymentAssistService.initMchAndApp(cashierCode.getMchNo(), cashierCode.getAppId());
        // 获取认证策略
        var channelAuthStrategy = PaymentStrategyFactory.create(sceneConfig.getChannel(), AbsChannelAuthStrategy.class);
        var cashierCodeType = CashierSceneEnum.findByCode(sceneConfig.getScene());
        return switch (cashierCodeType) {
            case WECHAT_PAY -> {
                var authUrlParam = new GenerateAuthUrlParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthPath(StrUtil.format("/wechat/cashier/code/{}", cashierCode.getCode()));
                yield channelAuthStrategy.generateAuthUrl(authUrlParam).getAuthUrl();
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }

    /**
     * 获取码牌认证结果
     */
    @IgnoreTenant
    public AuthResult auth(GatewayCashierCodeAuthParam param){
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(param.getCashierCode());
        // 查询码牌配置
        var sceneConfig = codeSceneConfigManager.findByConfigAndScene(cashierCode.getConfigId(), param.getCashierScene())
                .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));
        paymentAssistService.initMchAndApp(cashierCode.getMchNo(), cashierCode.getAppId());
        // 查询码牌支付场景配置
        var channelAuthStrategy = PaymentStrategyFactory.create(sceneConfig.getChannel(), AbsChannelAuthStrategy.class);
        var cashierCodeType = CashierSceneEnum.findByCode(sceneConfig.getScene());
        return switch (cashierCodeType) {
            case WECHAT_PAY -> {
                var codeParam = new AuthCodeParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthCode(param.getAuthCode());
                yield channelAuthStrategy.doAuth(codeParam);
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }
}
