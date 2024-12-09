package org.dromara.daxpay.service.service.checkout;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.core.enums.CheckoutCallTypeEnum;
import org.dromara.daxpay.core.enums.CheckoutTypeEnum;
import org.dromara.daxpay.core.enums.PayMethodEnum;
import org.dromara.daxpay.core.exception.ChannelNotExistException;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.core.exception.TradeProcessingException;
import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.checkout.*;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.checkout.CheckoutUrlResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutAggregateConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutItemConfigManager;
import org.dromara.daxpay.service.entity.config.PlatformConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutAggregateConfig;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.config.PlatformConfigService;
import org.dromara.daxpay.service.service.trade.pay.PayService;
import org.dromara.daxpay.service.strategy.AbsCheckoutStrategy;
import org.dromara.daxpay.service.util.PaymentStrategyFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 收银台服务
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CheckoutAssistService checkoutAssistService;
    private final LockTemplate lockTemplate;
    private final PlatformConfigService platformConfigService;
    private final PaymentAssistService paymentAssistService;
    private final CheckoutItemConfigManager checkoutItemConfigManager;
    private final PayService payService;
    private final CheckoutAggregateConfigManager checkoutAggregateConfigManager;

    /**
     * 生成收银台链接
     */
    public CheckoutUrlResult create(CheckoutCreatParam checkoutParam){
        // 校验支付限额
        checkoutAssistService.validationLimitAmount(checkoutParam);
        // 校验超时时间, 不可早于当前
        checkoutAssistService.validationExpiredTime(checkoutParam);
        // 获取商户订单号
        String bizOrderNo = checkoutParam.getBizOrderNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:pay:" + bizOrderNo,10000,200);
        if (Objects.isNull(lock)){
            log.warn("正在发起调起收银台中，请勿重复操作");
            throw new TradeProcessingException("正在发起调起收银台中，请勿重复操作");
        }
        try {
            // 查询并检查订单
            PayOrder payOrder = checkoutAssistService.getOrderAndCheck(checkoutParam);
            // 订单已经存在直接返回链接, 不存在创建订单后返回链接
            if (Objects.isNull(payOrder)){
                // 执行支付前的保存动作, 保存支付订单和扩展记录
                payOrder = checkoutAssistService.createPayOrder(checkoutParam);
            }
            String checkoutUrl = this.getCheckoutUrl(payOrder.getOrderNo(), checkoutParam.getCheckoutType());
            return new CheckoutUrlResult().setUrl(checkoutUrl);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 获取收银台链接
     */
    public String getCheckoutUrl(String orderNo, String checkoutType){
        CheckoutTypeEnum checkoutTypeEnum = CheckoutTypeEnum.findBuyCode(checkoutType);
        PlatformConfig config = platformConfigService.getConfig();
        switch (checkoutTypeEnum) {
            case H5 -> {
                return StrUtil.format("{}/checkout/{}",config.getGatewayMobileUrl(), orderNo);
            }
            case PC -> {
                return StrUtil.format("{}/checkout/{}",config.getGatewayPcUrl(), orderNo);
            }
            case AGGREGATE -> {
                return StrUtil.format("{}/aggregate/{}",config.getGatewayMobileUrl(), orderNo);
            }
            case MINI_APP -> throw new UnsupportedAbilityException("暂不支持小程序收银台");
            default -> throw new UnsupportedAbilityException("不支持的收银台类型");
        }
    }

    /**
     * 支付调用
     */
    public PayResult pay(CheckoutPayParam param){
        // 订单信息
        PayOrder payOrder = checkoutAssistService.getOrderAndCheck(param.getOrderNo());
        paymentAssistService.initMchApp(payOrder.getAppId());
        // 获取配置项
        var itemConfig = checkoutItemConfigManager.findByIdAndAppId(param.getItemId(),payOrder.getAppId())
                .orElseThrow(() -> new TradeProcessingException("支付配置项不存在"));
        // 判断支付调用类型
        CheckoutCallTypeEnum callTypeEnum = CheckoutCallTypeEnum.findBuyCode(itemConfig.getCallType());
        switch (callTypeEnum) {
            case QR_CODE, LINK, BAR_CODE,JSAPI,FROM -> {
                return this.checkoutPay(param, payOrder);
            }
            default -> throw new UnsupportedAbilityException("不支持的支付调用类型");
        }
    }

    /**
     * 处理参数使用通用支付接口调起支付
     */
    private PayResult checkoutPay(CheckoutPayParam param, PayOrder payOrder){
        // 查询配置
        CheckoutItemConfig itemConfig = checkoutItemConfigManager.findByIdAndAppId(param.getItemId(),payOrder.getAppId())
                .orElseThrow(() -> new TradeProcessingException("支付配置项不存在"));
        paymentAssistService.initMchApp(payOrder.getAppId());
        // 构建支付参数
        String clientIP = JakartaServletUtil.getClientIP(WebServletUtil.getRequest());
        PayParam payParam = new PayParam();
        payParam.setChannel(itemConfig.getChannel());
        payParam.setMethod(itemConfig.getPayMethod());
        payParam.setAppId(itemConfig.getAppId());
        payParam.setClientIp(clientIP);
        payParam.setReqTime(LocalDateTime.now());

        // 获取策略
        var cashierStrategy = PaymentStrategyFactory.create(itemConfig.getChannel(), AbsCheckoutStrategy.class);
        // 进行参数预处理
        cashierStrategy.handlePayParam(param, payParam);
        // 发起支付
        return payService.pay(payParam, payOrder);
    }

    /**
     * 聚合支付(扫码)
     */
    public PayResult aggregatePay(CheckoutAggregatePayParam param){
        // 订单信息
        PayOrder payOrder = checkoutAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合类型
        CheckoutAggregateConfig aggregateConfig = checkoutAggregateConfigManager.findByAppIdAndType(payOrder.getAppId(), param.getAggregateType())
                .orElseThrow(() -> new ConfigNotExistException("聚合支付配置项不存在"));
        paymentAssistService.initMchApp(payOrder.getAppId());
        // 构建支付参数
        String clientIP = JakartaServletUtil.getClientIP(WebServletUtil.getRequest());
        PayParam payParam = new PayParam();
        payParam.setChannel(aggregateConfig.getChannel());
        payParam.setMethod(aggregateConfig.getPayMethod());
        payParam.setAppId(aggregateConfig.getAppId());
        payParam.setClientIp(clientIP);
        payParam.setReqTime(LocalDateTime.now());

        // 获取策略
        var cashierStrategy = PaymentStrategyFactory.create(aggregateConfig.getChannel(), AbsCheckoutStrategy.class);
        // 进行参数预处理
        CheckoutPayParam cashierPayParam = new CheckoutPayParam()
                .setOpenId(param.getOpenId());
        cashierStrategy.handlePayParam(cashierPayParam, payParam);
        // 发起支付
        return payService.pay(payParam, payOrder);
    }

    /**
     * 聚合支付(条码支付)
     */
    public PayResult aggregateBarPay(CheckoutAggregateBarPayParam param){
        // 订单信息
        PayOrder payOrder = checkoutAssistService.getOrderAndCheck(param.getOrderNo());
        var cashierStrategies = PaymentStrategyFactory.createGroup(AbsCheckoutStrategy.class);
        // 获取策略
        var cashierStrategy = cashierStrategies.stream()
                .filter(strategy -> strategy.checkBarCode(param.getBarCode()))
                .findFirst()
                .orElseThrow(() -> new ChannelNotExistException("未找到支持该付款码的支付通道"));

        // 获取聚合类型
        paymentAssistService.initMchApp(payOrder.getAppId());
        // 构建支付参数
        String clientIP = JakartaServletUtil.getClientIP(WebServletUtil.getRequest());
        PayParam payParam = new PayParam();
        payParam.setChannel(cashierStrategy.getChannel());
        payParam.setMethod(PayMethodEnum.BARCODE.getCode());
        payParam.setAppId(payOrder.getAppId());
        payParam.setClientIp(clientIP);
        payParam.setReqTime(LocalDateTime.now());

        // 进行参数预处理
        CheckoutPayParam cashierPayParam = new CheckoutPayParam()
                .setBarCode(param.getBarCode());
        cashierStrategy.handlePayParam(cashierPayParam, payParam);
        // 发起支付
        return payService.pay(payParam, payOrder);
    }

    /**
     * 生成授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    public String generateAuthUrl(CheckoutAuthUrlParam param){
        // 订单信息
        PayOrder payOrder = checkoutAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合类型
        var aggregateConfig = checkoutAggregateConfigManager.findByAppIdAndType(payOrder.getAppId(), param.getAggregateType())
                .orElseThrow(() -> new ConfigNotExistException("聚合支付配置项不存在"));
        paymentAssistService.initMchApp(aggregateConfig.getAppId());
        // 获取策略
        var checkoutStrategy = PaymentStrategyFactory.create(aggregateConfig.getChannel(), AbsCheckoutStrategy.class);
        return checkoutStrategy.generateAuthUrl(param.getOrderNo());
    }

    /**
     * 授权结果
     */
    public AuthResult auth(CheckoutAuthCodeParam param) {
        // 订单信息
        PayOrder payOrder = checkoutAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合类型
        var aggregateConfig = checkoutAggregateConfigManager.findByAppIdAndType(payOrder.getAppId(), param.getAggregateType())
                .orElseThrow(() -> new ConfigNotExistException("聚合支付配置项不存在"));
        paymentAssistService.initMchApp(payOrder.getAppId());
        // 获取策略
        var checkoutStrategy = PaymentStrategyFactory.create(aggregateConfig.getChannel(), AbsCheckoutStrategy.class);
        AuthCodeParam authCodeParam = new AuthCodeParam()
                .setAuthCode(param.getAuthCode());
        return checkoutStrategy.doAuth(authCodeParam);
    }
}
