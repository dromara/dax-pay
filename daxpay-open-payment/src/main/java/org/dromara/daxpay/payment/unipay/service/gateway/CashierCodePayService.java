package org.dromara.daxpay.payment.unipay.service.gateway;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.common.exception.UnsupportedAbilityException;
import org.dromara.daxpay.payment.common.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.device.convert.qrcode.CashierCodeConvert;
import org.dromara.daxpay.payment.device.service.qrcode.CashierCodeService;
import org.dromara.daxpay.payment.merchant.dao.gateway.CashierCodeConfigManager;
import org.dromara.daxpay.payment.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.payment.merchant.entity.info.Merchant;
import org.dromara.daxpay.payment.pay.result.gateway.CashierCodePayInfoResult;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PayService;
import org.dromara.daxpay.payment.pay.strategy.AbsChannelAuthStrategy;
import org.dromara.daxpay.payment.pay.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.payment.pay.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.unipay.enums.CashierSceneEnum;
import org.dromara.daxpay.payment.unipay.enums.ChannelAuthTypeEnum;
import org.dromara.daxpay.payment.unipay.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.payment.unipay.param.assist.AuthCodeParam;
import org.dromara.daxpay.payment.unipay.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.payment.unipay.param.gateway.CashierCodeAuthParam;
import org.dromara.daxpay.payment.unipay.param.gateway.CashierCodePayParam;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 码牌支付服务商
 * @author xxm
 * @since 2025/6/30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierCodePayService {

    private final CashierCodeConfigManager cashierCodeConfigManager;
    private final CashierCodeService cashierCodeService;
    private final PaymentAssistService paymentAssistService;
    private final PayService payService;
    private final MerchantManager merchantManager;

    /**
     * 收银码牌支付
     */
    public PayResult cashierCodePay(CashierCodePayParam param){
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(param.getCode());

        // 码牌名称
        var codeName = cashierCode.getName();
        if (StrUtil.isBlank(codeName)){
            Merchant merchant = merchantManager.findByMchNo(cashierCode.getMchNo())
                    .orElseThrow(() -> new DataNotExistException("商户信息不存在"));
            codeName = merchant.getMchName();
        }
        // 获取码牌配置, 看是否读取系统配置
        if (cashierCode.isReadSystem()){
            var cashierCodeConfig = cashierCodeConfigManager.findByAppIdNotTenant(cashierCode.getAppId())
                    .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));
            // 复制属性
            CashierCodeConvert.CONVERT.copy(cashierCodeConfig, cashierCode);
        }

        // 构建支付参数
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo(TradeNoGenerateUtil.pay());

        payParam.setTitle(StrUtil.format("{} 码牌收款: {}元", codeName, param.getAmount()));
        payParam.setLimitPay(cashierCode.getLimitPay());
        payParam.setDescription(param.getDescription());
        payParam.setAmount(param.getAmount());
        payParam.setOpenId(param.getOpenId());
        payParam.setReqTime(LocalDateTime.now());
        payParam.setAppId(cashierCode.getAppId());
        payParam.setMchNo(cashierCode.getMchNo());

        var cashierScene = CashierSceneEnum.findByCode(param.getScene());
        switch (cashierScene) {
            case WECHAT_PAY -> {
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(cashierCode.getWxChannel(), AbsGatewayPayStrategy.class);
                var payMethod = cashierCode.getWxMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.wechatPayMethods().getFirst().getCode();
                }
                payParam.setChannel(cashierCode.getWxChannel());
                payParam.setMethod(payMethod);
            }
            case ALIPAY -> {
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(cashierCode.getAlipayChannel(), AbsGatewayPayStrategy.class);
                var payMethod = cashierCode.getAlipayMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.alipayPayMethods().getFirst().getCode();
                }
                payParam.setChannel(cashierCode.getWxChannel());
                payParam.setMethod(payMethod);
            }
            case UNION_PAY -> {
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(cashierCode.getAlipayChannel(), AbsGatewayPayStrategy.class);
                var payMethod = cashierCode.getUnionMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.unionPayMethods().getFirst().getCode();
                }
                payParam.setChannel(cashierCode.getWxChannel());
                payParam.setMethod(payMethod);
            }
        }

        // 设置IP
        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("127.0.0.1");
        payParam.setClientIp(ip);

        // 发起支付
        paymentAssistService.initMchAndApp(cashierCode.getMchNo(), cashierCode.getAppId());
        return payService.pay(payParam);
    }

    /**
     * 根据码牌编码和类型查询信息和配置
     */
    public CashierCodePayInfoResult getCashierCodeConfig(String code, String scene) {
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(code);

        // 码牌名称
        var codeName = cashierCode.getName();
        if (StrUtil.isBlank(codeName)){
            Merchant merchant = merchantManager.findByMchNo(cashierCode.getMchNo())
                    .orElseThrow(() -> new DataNotExistException("商户信息不存在"));
            codeName = merchant.getMchName();
        }
        // 获取码牌配置, 看是否读取系统配置
        if (cashierCode.isReadSystem()){
            var cashierCodeConfig = cashierCodeConfigManager.findByAppIdNotTenant(cashierCode.getAppId())
                    .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));
            // 复制属性
            CashierCodeConvert.CONVERT.copy(cashierCodeConfig, cashierCode);
        }

        // 返回配置结果
        var codePayInfoResult = new CashierCodePayInfoResult()
                .setName(codeName)
                .setCode(code)
                .setAmountType(cashierCode.getAmountType())
                .setAmount(cashierCode.getAmount());

        // 获取支付场景
        var sceneEnum = CashierSceneEnum.findByCode(scene);
        // 根据类型选择对应的处理方式
        switch (sceneEnum) {
            case WECHAT_PAY ->{
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(cashierCode.getWxChannel(), AbsGatewayPayStrategy.class);
                var payMethod = cashierCode.getWxMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.wechatPayMethods().getFirst().getCode();
                }
                // 获取调用方式
                String callType = Optional.ofNullable(gatewayPayStrategy.getCallType(payMethod))
                        .map(GatewayCallTypeEnum::getCode)
                        .orElseThrow(() -> new UnsupportedAbilityException("当前码牌的支付方式无法调起支付!"));
                codePayInfoResult.setCallType(callType);
                // 是否需要获取OpenId
                codePayInfoResult.setNeedOpenId(gatewayPayStrategy.isNeedOpenIdAuth(payMethod));
            }
            case ALIPAY ->{
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(cashierCode.getAlipayMethod(), AbsGatewayPayStrategy.class);
                var payMethod = cashierCode.getAlipayMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.alipayPayMethods().getFirst().getCode();
                }
                // 获取调用方式
                String callType = Optional.ofNullable(gatewayPayStrategy.getCallType(payMethod))
                        .map(GatewayCallTypeEnum::getCode)
                        .orElseThrow(() -> new UnsupportedAbilityException("当前码牌的支付方式无法调起支付!"));
                codePayInfoResult.setCallType(callType);
            }
            case UNION_PAY ->{
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(cashierCode.getUnionChannel(), AbsGatewayPayStrategy.class);
                var payMethod = cashierCode.getUnionChannel();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.unionPayMethods().getFirst().getCode();
                }
                // 获取调用方式
                String callType = Optional.ofNullable(gatewayPayStrategy.getCallType(payMethod))
                        .map(GatewayCallTypeEnum::getCode)
                        .orElseThrow(() -> new UnsupportedAbilityException("当前码牌的支付方式无法调起支付!"));
                codePayInfoResult.setCallType(callType);
            }
        }
        return codePayInfoResult;
    }

    /**
     * 生成码牌所需授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @IgnoreTenant
    public String genAuthUrl(String code, String scene){
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(code);
        // 判断码牌是否读取系统配置
        String wxChannel;
        if (cashierCode.isReadSystem()){
            // 查询码牌支付场景配置
            var sceneConfig = cashierCodeConfigManager.findByAppIdNotTenant(cashierCode.getAppId())
                    .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));
            wxChannel = sceneConfig.getWxChannel();
        } else {
            wxChannel = cashierCode.getWxChannel();
        }
        // 初始化上下文信息用于调用微信认证
        paymentAssistService.initMchAndApp(cashierCode.getMchNo(), cashierCode.getAppId());
        // 获取认证策略
        var cashierCodeType = CashierSceneEnum.findByCode(scene);
        return switch (cashierCodeType) {
            case WECHAT_PAY -> {
                // 获取网关支付策略, 从而获取微信收银台授权路径
                var gatewayPayStrategy = PaymentStrategyFactory.create(wxChannel, AbsGatewayPayStrategy.class);
                var channelAuthStrategy = PaymentStrategyFactory.create(wxChannel, AbsChannelAuthStrategy.class);
                var authUrlParam = new GenerateAuthUrlParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthPath(gatewayPayStrategy.getWxCashierAuthPath(cashierCode.getCode()));
                yield channelAuthStrategy.generateAuthUrl(authUrlParam).getAuthUrl();
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }

    /**
     * 获取码牌认证结果
     */
    @IgnoreTenant
    public AuthResult auth(CashierCodeAuthParam param){
        // 查询并查询码牌
        var cashierCode = cashierCodeService.getAndCheckCode(param.getCode());
        // 判断码牌是否读取系统配置
        String wxChannel;
        if (cashierCode.isReadSystem()){
            // 查询码牌支付场景配置
            var sceneConfig = cashierCodeConfigManager.findByAppIdNotTenant(cashierCode.getAppId())
                    .orElseThrow(() -> new DataNotExistException("码牌支付场景配置不存在"));
            wxChannel = sceneConfig.getWxChannel();
        } else {
            wxChannel = cashierCode.getWxChannel();
        }
        paymentAssistService.initMchAndApp(cashierCode.getMchNo(), cashierCode.getAppId());
        // 初始化上下文信息用于调用微信认证
        var cashierCodeType = CashierSceneEnum.findByCode(param.getScene());
        return switch (cashierCodeType) {
            case WECHAT_PAY -> {
                var channelAuthStrategy = PaymentStrategyFactory.create(wxChannel, AbsChannelAuthStrategy.class);
                var codeParam = new AuthCodeParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthCode(param.getAuthCode());
                yield channelAuthStrategy.doAuth(codeParam);
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }
}
