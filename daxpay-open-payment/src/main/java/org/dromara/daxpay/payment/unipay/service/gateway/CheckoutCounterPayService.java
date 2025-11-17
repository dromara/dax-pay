package org.dromara.daxpay.payment.unipay.service.gateway;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import cn.bootx.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.payment.common.service.config.PlatformConfigService;
import org.dromara.daxpay.payment.merchant.result.gateway.CheckoutCounterConfigResult;
import org.dromara.daxpay.payment.merchant.service.gateway.CheckoutCounterConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayReadConfigService;
import org.dromara.daxpay.payment.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.pay.result.gateway.CheckoutCounterOrderAndConfigResult;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayOrderResult;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PayService;
import org.dromara.daxpay.payment.pay.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.payment.pay.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.unipay.enums.CheckoutCounterTypeEnum;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class CheckoutCounterPayService {
    private final PayService payService;
    private final GatewayPayAssistService gatewayPayAssistService;
    private final PaymentAssistService paymentAssistService;
    private final GatewayPayConfigService gatewayPayConfigService;
    private final CheckoutCounterConfigService checkoutCounterConfigService;
    private final PlatformConfigService platformConfigService;
    private final GatewayPayReadConfigService gatewayPayReadConfigService;

    /**
     * 网关收银台支付调用
     */
    @IgnoreTenant
    public PayResult cashierPay(String orderNo, Long configId){
        // 订单信息
        PayOrder payOrder = gatewayPayAssistService.getOrderAndCheck(orderNo);
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        // 读取收银台类目配置内容
        var counterConfig = checkoutCounterConfigService.findById(configId);
        // 拉起支付
        return this.payHandle(counterConfig, payOrder);
    }

    /**
     * 处理网关支付参数后使用通用支付接口调起支付
     */
    private PayResult payHandle(CheckoutCounterConfigResult itemConfig, PayOrder payOrder){
        // 初始化支付条件
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        // 构建支付参数
        var payParam = new PayParam();
        payParam.setChannel(itemConfig.getChannel());
        payParam.setMethod(itemConfig.getPayMethod());
        payParam.setAppId(payOrder.getAppId());
        payParam.setMchNo(payOrder.getMchNo());
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }

        // 发起支付
        return payService.payHandle(payParam,payOrder);
    }

    /**
     * 获取收银台配置, 包含明细配置
     */
    private List<CheckoutCounterConfigResult> getConfig(String appId, String type){
        var readConfig = gatewayPayReadConfigService.findByAppId(appId);
        var counterConfigs = checkoutCounterConfigService.getCheckoutCounterConfigs(appId, type, readConfig);
        var list = MpUtil.toListResult(counterConfigs);
        // 补充调起方式
        for (var counterConfig : list) {
            var gatewayPayStrategy = PaymentStrategyFactory.create(counterConfig.getChannel(), AbsGatewayPayStrategy.class);
            // TODO 需要扫码方式需要是显示二维码
            var callType = gatewayPayStrategy.getCallType(counterConfig.getPayMethod());
            counterConfig.setCallType(callType.getCode());
        }
        return list;
    }

    /**
     * 获取收银台配置和订单相关信息, 不需要签名和鉴权, 忽略租户拦截
     */
    @IgnoreTenant
    public CheckoutCounterOrderAndConfigResult getOrderAndConfig(String orderNo, String type){
        var gatewayInfo = new CheckoutCounterOrderAndConfigResult();
        // 订单信息
        var payOrder = Optional.ofNullable(gatewayPayAssistService.getOrderAndCheck(orderNo))
                .orElseThrow(() -> new DataNotExistException("订单不存在"));
        var order = new GatewayOrderResult()
                .setTitle(payOrder.getTitle())
                .setDescription(payOrder.getDescription())
                .setAmount(payOrder.getAmount())
                .setBizOrderNo(payOrder.getBizOrderNo())
                .setExpiredTime(payOrder.getExpiredTime())
                .setOrderNo(payOrder.getOrderNo());
        gatewayInfo.setOrder(order);
        // 获取网关支付配置
        var gatewayConfig = gatewayPayConfigService.getGatewayConfig(payOrder.getAppId());
        gatewayInfo.setConfig(gatewayConfig.toResult());
        // 获取收银台配置列表
        var counterConfigs = this.getConfig(payOrder.getAppId(), type);
        gatewayInfo.setCounterConfigs(counterConfigs);
        // 判断是否需要显示聚合扫码支付链接 同时满足 pc类型 + 开启显示
        if (Objects.equals(type, CheckoutCounterTypeEnum.PC.getCode())
                &&gatewayInfo.getConfig().isAggregateQrShow()){
            // 拼接聚合支付链接, 和网关支付中 gatewayUrl() 逻辑一致
            var config = platformConfigService.getUrlConfig();
            String url = StrUtil.format("{}/Checkout/{}", config.getGatewayH5Url(), payOrder.getOrderNo());
            gatewayInfo.setAggregateUrl(url);
        }
        return gatewayInfo;
    }
}
