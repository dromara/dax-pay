package org.dromara.daxpay.service.pay.service.gateway;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.core.enums.GatewayCallTypeEnum;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.core.exception.TradeProcessingException;
import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.core.param.gateway.GatewayCashierBarPayParam;
import org.dromara.daxpay.core.param.gateway.GatewayCashierPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.merchant.entity.gateway.CashierItemConfig;
import org.dromara.daxpay.service.merchant.entity.gateway.GatewayPayConfig;
import org.dromara.daxpay.service.merchant.service.gateway.AggregateConfigService;
import org.dromara.daxpay.service.merchant.service.gateway.CashierConfigService;
import org.dromara.daxpay.service.merchant.service.gateway.GatewayPayConfigService;
import org.dromara.daxpay.service.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.pay.service.trade.pay.PayService;
import org.dromara.daxpay.service.pay.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.service.pay.util.PaymentStrategyFactory;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private final PayService payService;
    private final GatewayPayAssistService gatewayPayAssistService;
    private final PaymentAssistService paymentAssistService;
    private final GatewayPayConfigService gatewayPayConfigService;
    private final AggregateConfigService aggregateConfigService;
    private final CashierConfigService cashierConfigService;

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
        GatewayPayConfig gatewayConfig = gatewayPayConfigService.getGatewayConfig(payOrder.getAppId());
        var barPayConfigs = aggregateConfigService.getAggregateBarPayConfigs(payOrder.getAppId(), aggregatePayType.getCode(), gatewayConfig);
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
        // 获取收银台配置
        var cashierConfig =  gatewayPayConfigService.getGatewayConfig(payOrder.getAppId());
        // 读取收银台类目配置内容
        var itemConfig = cashierConfigService.getCashierItemConfig(param.getItemId(),payOrder.getAppId(), cashierConfig)
                    .orElseThrow(() -> new TradeProcessingException("支付配置项不存在"));
        // 判断支付调用类型
        var callTypeEnum = GatewayCallTypeEnum.findBuyCode(itemConfig.getCallType());
        switch (callTypeEnum) {
            case QR_CODE, LINK, BAR_CODE, JSAPI, FROM -> {
                return this.payHandle(param, itemConfig, payOrder);
            }
            default -> throw new UnsupportedAbilityException("不支持的支付调用类型");
        }
    }

    /**
     * 处理网关支付参数后使用通用支付接口调起支付
     */
    private PayResult payHandle(GatewayCashierPayParam param, CashierItemConfig itemConfig, PayOrder payOrder){
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

}
