package org.dromara.daxpay.payment.unipay.service.gateway;

import cn.bootx.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.payment.common.exception.UnsupportedAbilityException;
import org.dromara.daxpay.payment.merchant.service.gateway.AggregateConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayReadConfigService;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.result.gateway.AggregateOrderAndConfigResult;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayOrderResult;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.strategy.AbsChannelAuthStrategy;
import org.dromara.daxpay.payment.pay.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.payment.pay.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.unipay.enums.CashierSceneEnum;
import org.dromara.daxpay.payment.unipay.enums.ChannelAuthTypeEnum;
import org.dromara.daxpay.payment.unipay.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.payment.unipay.param.assist.AuthCodeParam;
import org.dromara.daxpay.payment.unipay.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.payment.unipay.param.gateway.CheckoutCounterAuthCodeParam;
import org.dromara.daxpay.payment.unipay.result.assist.AuthResult;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 聚合支付服务
 * @author xxm
 * @since 2025/10/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateQrPayService {
    private final GatewayPayAssistService gatewayPayAssistService;
    private final GatewayPayConfigService gatewayPayConfigService;
    private final AggregateConfigService aggregateConfigService;
    private final PaymentAssistService paymentAssistService;
    private final GatewayPayReadConfigService gatewayPayReadConfigService;


    /**
     * 聚合支付配置
     */
    @IgnoreTenant
    public AggregateOrderAndConfigResult getQrPayConfig(String orderNo, String scene){
        var gatewayInfoResult = new AggregateOrderAndConfigResult();
        // 订单信息
        PayOrder payOrder = gatewayPayAssistService.getOrderAndCheck(orderNo);
        GatewayOrderResult order = new GatewayOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setExpiredTime(payOrder.getExpiredTime())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setOrderNo(payOrder.getOrderNo());
        gatewayInfoResult.setOrder(order);
        // 获取网关支付配置配置
        var readConfig = gatewayPayReadConfigService.findByAppId(payOrder.getAppId());
        // 获取聚合扫码支付配置
        var qrPayConfig = aggregateConfigService.getQrPayConfig(payOrder.getAppId(), readConfig).toResult();
        gatewayInfoResult.setAutoLaunch(qrPayConfig.isAutoLaunch());
        // 获取支付场景
        var sceneEnum = CashierSceneEnum.findByCode(scene);
        // 根据类型选择对应的处理方式
        switch (sceneEnum) {
            case WECHAT_PAY -> {
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(qrPayConfig.getWxChannel(), AbsGatewayPayStrategy.class);
                var payMethod = qrPayConfig.getWxMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.wechatPayMethods().getFirst().getCode();
                }
                // 获取调用方式
                String callType = Optional.ofNullable(gatewayPayStrategy.getCallType(payMethod))
                        .map(GatewayCallTypeEnum::getCode)
                        .orElseThrow(() -> new UnsupportedAbilityException("当前聚合扫码配置的支付方式无法调起支付!"));
                gatewayInfoResult.setCallType(callType);
                // 是否需要获取OpenId
                gatewayInfoResult.setNeedOpenId(gatewayPayStrategy.isNeedOpenIdAuth(payMethod));

            }
            case ALIPAY -> {
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(qrPayConfig.getAlipayChannel(), AbsGatewayPayStrategy.class);
                var payMethod = qrPayConfig.getAlipayMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.alipayPayMethods().getFirst().getCode();
                }
                // 获取调用方式
                String callType = Optional.ofNullable(gatewayPayStrategy.getCallType(payMethod))
                        .map(GatewayCallTypeEnum::getCode)
                        .orElseThrow(() -> new UnsupportedAbilityException("当前聚合扫码配置的支付方式无法调起支付!"));
                gatewayInfoResult.setCallType(callType);

            }
            case UNION_PAY -> {
                // 判断支付方式是否配置
                var gatewayPayStrategy = PaymentStrategyFactory.create(qrPayConfig.getUnionChannel(), AbsGatewayPayStrategy.class);
                var payMethod = qrPayConfig.getUnionMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = gatewayPayStrategy.unionPayMethods().getFirst().getCode();
                }
                // 获取调用方式
                String callType = Optional.ofNullable(gatewayPayStrategy.getCallType(payMethod))
                        .map(GatewayCallTypeEnum::getCode)
                        .orElseThrow(() -> new UnsupportedAbilityException("当前聚合扫码配置的支付方式无法调起支付!"));
                gatewayInfoResult.setCallType(callType);
            }
        }
        return gatewayInfoResult;
    }


    /**
     * 生成聚合所需授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @IgnoreTenant
    public String genAuthUrl(String orderNo, String scene){
        // 订单信息
        var payOrder = gatewayPayAssistService.getOrderAndCheck(orderNo);
        // 获取聚合类型
        var readConfig = gatewayPayReadConfigService.findByAppId(payOrder.getAppId());
        var aggregateConfig = aggregateConfigService.getQrPayConfig(payOrder.getAppId(), readConfig);
        paymentAssistService.initMchAndApp(payOrder.getAppId());
        var counterType = CashierSceneEnum.findByCode(scene);
        return switch (counterType) {
            case WECHAT_PAY -> {
                // 获取聚合支付微信认证路径
                var gatewayPayStrategy = PaymentStrategyFactory.create(aggregateConfig.getWxChannel(), AbsGatewayPayStrategy.class);
                var channelAuthStrategy = PaymentStrategyFactory.create(aggregateConfig.getWxChannel(), AbsChannelAuthStrategy.class);
                var authUrlParam = new GenerateAuthUrlParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthPath(gatewayPayStrategy.getWxAggregateAuthPath(orderNo));
                yield channelAuthStrategy.generateAuthUrl(authUrlParam).getAuthUrl();
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }

    /**
     * 网关支付授权结果
     */
    @IgnoreTenant
    public AuthResult auth(CheckoutCounterAuthCodeParam param) {
        // 订单信息
        var payOrder = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合类型
        var readConfig = gatewayPayReadConfigService.findByAppId(payOrder.getAppId());
        var aggregateConfig = aggregateConfigService.getQrPayConfig(payOrder.getAppId(), readConfig);
        paymentAssistService.initMchAndApp(payOrder.getAppId());
        // 获取认证策略
        var counterType = CashierSceneEnum.findByCode(param.getScene());
        return switch (counterType) {
            case WECHAT_PAY -> {
                var channelAuthStrategy = PaymentStrategyFactory.create(aggregateConfig.getWxChannel(), AbsChannelAuthStrategy.class);
                var codeParam = new AuthCodeParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthCode(param.getAuthCode());
                yield channelAuthStrategy.doAuth(codeParam);
            }
            case ALIPAY,UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }

}
