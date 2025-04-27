package org.dromara.daxpay.service.service.gateway;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.core.enums.CashierCodeTypeEnum;
import org.dromara.daxpay.core.enums.ChannelAuthTypeEnum;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.core.exception.TradeProcessingException;
import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.param.gateway.*;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.dao.gateway.AggregateBarPayConfigManager;
import org.dromara.daxpay.service.dao.gateway.CashierCodeConfigManager;
import org.dromara.daxpay.service.dao.gateway.CashierCodeItemConfigManager;
import org.dromara.daxpay.service.dao.gateway.CashierItemConfigManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.trade.pay.PayService;
import org.dromara.daxpay.service.strategy.AbsChannelAuthStrategy;
import org.dromara.daxpay.service.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 网关收银台支付服务
 * @author xxm
 * @since 2025/4/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierPayService {

    private final GatewayPayAssistService gatewayPayAssistService;
    private final PaymentAssistService paymentAssistService;
    private final AggregateBarPayConfigManager aggregateBarPayConfigManager;
    private final PayService payService;
    private final CashierCodeConfigManager cashierCodeConfigManager;
    private final CashierCodeItemConfigManager cashierCodeItemConfigManager;
    private final CashierItemConfigManager cashierItemConfigManager;

    /**
     * 收银台聚合支付(付款码)
     */
    @IgnoreTenant
    public PayResult cashierBarPay(GatewayCashierBarPayParam param) {
        var payOrder = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        // 识别付款码类型
        var aggregatePayType = PayUtil.getBarCodeType(param.getAuthCode());
        // 获取聚合付款码支付配置
        var barPayConfigs = aggregateBarPayConfigManager.findByAppIdAndType(payOrder.getAppId(), aggregatePayType.getCode());
        if (CollUtil.isEmpty(barPayConfigs)){
            throw new ConfigNotExistException("未找到支持该付款码的支付通道");
        }
        // 获取支付策略
        var barPayConfig = barPayConfigs.getFirst();
        var payParam = new PayParam();
        payParam.setChannel(barPayConfig.getChannel());
        payParam.setMethod(barPayConfig.getBarPayType());
        payParam.setOtherMethod(barPayConfig.getOtherMethod());
        payParam.setAuthCode(param.getAuthCode());
        payParam.setAppId(barPayConfig.getAppId());
        payParam.setMchNo(barPayConfig.getMchNo());
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }
        return payService.payHandle(payParam,payOrder);
    }

    /**
     * 网关收银台支付调用
     */
    @IgnoreTenant
    public PayResult cashierPay(GatewayCashierPayParam param){
        // 订单信息
        PayOrder payOrder = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        // 获取配置项
        var itemConfig = cashierItemConfigManager.findByIdAndAppId(param.getItemId(),payOrder.getAppId())
                .orElseThrow(() -> new TradeProcessingException("支付配置项不存在"));
        // 判断支付调用类型
        var callTypeEnum = GatewayCallTypeEnum.findBuyCode(itemConfig.getCallType());
        switch (callTypeEnum) {
            case QR_CODE, LINK, BAR_CODE, JSAPI, FROM -> {
                return this.payHandle(param, payOrder);
            }
            default -> throw new UnsupportedAbilityException("不支持的支付调用类型");
        }
    }

    /**
     * 处理网关支付参数后使用通用支付接口调起支付
     */
    private PayResult payHandle(GatewayCashierPayParam param, PayOrder payOrder){
        // 查询配置
        var itemConfig = cashierItemConfigManager.findByIdAndAppId(param.getItemId(),payOrder.getAppId())
                .orElseThrow(() -> new TradeProcessingException("支付配置项不存在"));
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        // 构建支付参数
        var payParam = new PayParam();
        payParam.setChannel(itemConfig.getChannel());
        payParam.setMethod(itemConfig.getPayMethod());
        payParam.setOtherMethod(itemConfig.getOtherMethod());
        payParam.setAuthCode(param.getAuthCode());
        payParam.setOpenId(param.getOpenId());
        payParam.setAppId(itemConfig.getAppId());
        payParam.setMchNo(payOrder.getMchNo());
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }

        // 获取策略
        var cashierStrategy = PaymentStrategyFactory.create(itemConfig.getChannel(), AbsGatewayPayStrategy.class);
        // 进行参数预处理
        cashierStrategy.handlePayParam(param, payParam);
        // 发起支付
        return payService.payHandle(payParam,payOrder);
    }

    /**
     * 收银码牌支付
     */
    @IgnoreTenant
    public PayResult cashierCodePay(GatewayCashierCodePayParam param){
        // 查询码牌配置
        var codeConfig = cashierCodeConfigManager.findByCode(param.getCashierCode())
                .orElseThrow(() -> new DataNotExistException("码牌不存在"));
        // 是否启用
        if (!codeConfig.getEnable()) {
            throw new ConfigNotEnableException("支付码牌已禁用");
        }
        paymentAssistService.initMchAndApp(codeConfig.getMchNo(), codeConfig.getAppId());
        var itemConfig = cashierCodeItemConfigManager.findByCodeAndType(codeConfig.getId(), param.getCashierType())
                .orElseThrow(() -> new DataNotExistException("码牌配置不存在"));
        // 构建支付参数
        PayParam payParam = new PayParam();
        payParam.setBizOrderNo(TradeNoGenerateUtil.pay());

        payParam.setChannel(itemConfig.getChannel());
        payParam.setMethod(itemConfig.getPayMethod());
        payParam.setOtherMethod(itemConfig.getOtherMethod());
        payParam.setAppId(itemConfig.getAppId());
        payParam.setTitle(StrUtil.format("手机收银金额: {}元", param.getAmount()));
        payParam.setLimitPay(codeConfig.getLimitPay());
        payParam.setDescription(param.getDescription());
        payParam.setAmount(param.getAmount());
        payParam.setOpenId(param.getOpenId());
        payParam.setReqTime(LocalDateTime.now());
        // 设置IP
        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("127.0.0.1");
        payParam.setClientIp(ip);

        // 发起支付
        return payService.pay(payParam);
    }


    /**
     * 生成码牌所需授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @IgnoreTenant
    public String genAuthUrl(GatewayCashierCodeAuthUrlParam param){
        // 查询码牌配置
        var codeConfig = cashierCodeConfigManager.findByCode(param.getCashierCode())
                .orElseThrow(() -> new DataNotExistException("码牌不存在"));
        // 是否启用
        if (!codeConfig.getEnable()) {
            throw new ConfigNotEnableException("支付码牌已禁用");
        }
        paymentAssistService.initMchAndApp(codeConfig.getMchNo(), codeConfig.getAppId());
        var codeItemConfig = cashierCodeItemConfigManager.findByCodeAndType(codeConfig.getId(), param.getCashierType())
                .orElseThrow(() -> new DataNotExistException("码牌配置不存在"));
        // 获取认证策略
        var channelAuthStrategy = PaymentStrategyFactory.create(codeItemConfig.getChannel(), AbsChannelAuthStrategy.class);
        var cashierCodeType = CashierCodeTypeEnum.findByCode(codeItemConfig.getType());
        return switch (cashierCodeType) {
            case WECHAT_PAY -> {
                var authUrlParam = new GenerateAuthUrlParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthPath(StrUtil.format("/wechat/cashier/code/{}", codeConfig.getCode()));
                yield channelAuthStrategy.generateAuthUrl(authUrlParam).getAuthUrl();
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };

    }

    /**
     * 获取认证结果
     */
    @IgnoreTenant
    public AuthResult auth(GatewayCashierCodeAuthParam param){
        // 查询码牌配置
        var codeConfig = cashierCodeConfigManager.findByCode(param.getCashierCode())
                .orElseThrow(() -> new DataNotExistException("码牌不存在"));
        // 是否启用
        if (!codeConfig.getEnable()) {
            throw new ConfigNotEnableException("支付码牌已禁用");
        }
        paymentAssistService.initMchAndApp(codeConfig.getMchNo(), codeConfig.getAppId());
        var codeItemConfig = cashierCodeItemConfigManager.findByCodeAndType(codeConfig.getId(), param.getCashierType())
                .orElseThrow(() -> new DataNotExistException("码牌配置不存在"));
        // 获取认证策略
        var channelAuthStrategy = PaymentStrategyFactory.create(codeItemConfig.getChannel(), AbsChannelAuthStrategy.class);
        var cashierCodeType = CashierCodeTypeEnum.findByCode(codeItemConfig.getType());
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
