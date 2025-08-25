package org.dromara.daxpay.service.pay.service.gateway;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.core.context.PaymentReqInfoLocal;
import org.dromara.daxpay.core.enums.AggregatePayTypeEnum;
import org.dromara.daxpay.core.enums.ChannelAuthTypeEnum;
import org.dromara.daxpay.core.enums.GatewayPayTypeEnum;
import org.dromara.daxpay.core.exception.ConfigNotExistException;
import org.dromara.daxpay.core.exception.TradeProcessingException;
import org.dromara.daxpay.core.exception.UnsupportedAbilityException;
import org.dromara.daxpay.core.param.aggregate.AggregateBarPayParam;
import org.dromara.daxpay.core.param.aggregate.AggregatePayParam;
import org.dromara.daxpay.core.param.assist.AuthCodeParam;
import org.dromara.daxpay.core.param.assist.GenerateAuthUrlParam;
import org.dromara.daxpay.core.param.gateway.GatewayAuthCodeParam;
import org.dromara.daxpay.core.param.gateway.GatewayAuthUrlParam;
import org.dromara.daxpay.core.param.gateway.GatewayCashierPayParam;
import org.dromara.daxpay.core.param.gateway.GatewayPayParam;
import org.dromara.daxpay.core.param.trade.pay.PayParam;
import org.dromara.daxpay.core.result.assist.AuthResult;
import org.dromara.daxpay.core.result.trade.pay.PayResult;
import org.dromara.daxpay.core.util.PayUtil;
import org.dromara.daxpay.service.merchant.service.gateway.AggregateConfigService;
import org.dromara.daxpay.service.merchant.service.gateway.GatewayPayConfigService;
import org.dromara.daxpay.service.pay.common.local.PaymentContextLocal;
import org.dromara.daxpay.service.pay.result.gateway.GatewayPayUrlResult;
import org.dromara.daxpay.service.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.pay.service.trade.pay.PayAssistService;
import org.dromara.daxpay.service.pay.service.trade.pay.PayService;
import org.dromara.daxpay.service.pay.strategy.AbsChannelAuthStrategy;
import org.dromara.daxpay.service.pay.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.service.pay.util.PaymentStrategyFactory;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 网关收银台服务
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayPayService {
    private final GatewayPayAssistService gatewayPayAssistService;
    private final LockTemplate lockTemplate;
    private final PaymentAssistService paymentAssistService;
    private final PayService payService;
    private final PayAssistService payAssistService;
    private final AggregateConfigService aggregateConfigService;
    private final GatewayPayConfigService gatewayPayConfigService;

    /**
     * 预支付: 生成收银台链接
     */
    public GatewayPayUrlResult prePay(GatewayPayParam payParam){
        // 校验支付限额
        payAssistService.validationLimitAmount(payParam.getAmount());
        // 校验超时时间, 不可早于当前
        payAssistService.validationExpiredTime(payParam.getExpiredTime());
        // 获取商户订单号
        String bizOrderNo = payParam.getBizOrderNo();
        // 加锁
        LockInfo lock = lockTemplate.lock("payment:gateway:pay" + bizOrderNo,10000,200);
        if (Objects.isNull(lock)){
            log.warn("正在调起网关支付收银台中，请勿重复操作");
            throw new TradeProcessingException("正在调起收银台网关支付中，请勿重复操作");
        }
        try {
            // 检查支付订单
            var payOrder = payAssistService.getOrderAndCheck(payParam.getBizOrderNo(), payParam.getAppId());
            // 订单已经存在直接返回链接, 不存在创建订单后返回链接
            if (Objects.isNull(payOrder)){
                // 执行支付前的保存动作, 保存支付订单和扩展记录
                payOrder = gatewayPayAssistService.createPayOrder(payParam);
            }
            String checkoutUrl = this.getGatewayUrl(payOrder.getOrderNo(), payParam.getGatewayPayType());
            return new GatewayPayUrlResult().setUrl(checkoutUrl);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /**
     * 获取网关支付链接
     */
    public String getGatewayUrl(String orderNo, String gatewayPayType){
        var gatewayPayTypeEnum = GatewayPayTypeEnum.findBuyCode(gatewayPayType);
        PaymentReqInfoLocal reqInfo = PaymentContextLocal.get().getReqInfo();
        switch (gatewayPayTypeEnum) {
            case H5 -> {
                return StrUtil.format("{}/cashier/{}",reqInfo.getGatewayH5Url(), orderNo);
            }
            case PC -> {
                return StrUtil.format("{}/pc/cashier/{}",reqInfo.getGatewayH5Url(), orderNo);
            }
            case AGGREGATE -> {
                return StrUtil.format("{}/aggregate/{}",reqInfo.getGatewayH5Url(), orderNo);
            }
            case MINI_APP -> throw new UnsupportedAbilityException("暂不支持小程序收银台");
            default -> throw new UnsupportedAbilityException("不支持的网关支付类型");
        }
    }

    /**
     * 网关聚合支付(扫码/浏览器访问)
     */
    @IgnoreTenant
    public PayResult aggregatePay(AggregatePayParam param){
        // 订单信息
        var payOrder = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合类型
        var gatewayConfig = gatewayPayConfigService.getGatewayConfig(payOrder.getAppId());
        var aggregateConfig = aggregateConfigService.getAggregatePayConfig(payOrder.getAppId(), param.getAggregateType(), gatewayConfig);
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        // 构建支付参数
        var payParam = new PayParam();
        payParam.setChannel(aggregateConfig.getChannel());
        payParam.setMethod(aggregateConfig.getPayMethod());
        payParam.setOtherMethod(aggregateConfig.getOtherMethod());
        payParam.setOpenId(param.getOpenId());
        payParam.setAppId(aggregateConfig.getAppId());
        payParam.setMchNo(payOrder.getMchNo());
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }
        // 获取策略
        var cashierStrategy = PaymentStrategyFactory.create(aggregateConfig.getChannel(), AbsGatewayPayStrategy.class);
        // 进行参数预处理
        var cashierPayParam = new GatewayCashierPayParam().setOpenId(param.getOpenId());
        cashierStrategy.handlePayParam(cashierPayParam, payParam);
        // 发起支付
        return payService.payHandle(payParam,payOrder);
    }

    /**
     * 网关聚合支付(付款码)
     */
    public PayResult aggregateBarPay(AggregateBarPayParam param){
        // 识别付款码类型
        var aggregatePayType = PayUtil.getBarCodeType(param.getAuthCode());
        // 获取聚合付款码支付配置
        var gatewayConfig = gatewayPayConfigService.getGatewayConfig(param.getAppId());
        var barPayConfigs = aggregateConfigService.getAggregateBarPayConfigs(param.getAppId(), aggregatePayType.getCode(), gatewayConfig);
        if (CollUtil.isEmpty(barPayConfigs)){
            throw new ConfigNotExistException("未找到支持该付款码的支付通道");
        }
        // 获取支付策略
        var barPayConfig = barPayConfigs.getFirst();
        // 构建支付参数
        var payParam = new PayParam();
        BeanUtil.copyProperties(param, payParam);
        payParam.setChannel(barPayConfig.getChannel());
        payParam.setMethod(barPayConfig.getPayMethod());
        payParam.setOtherMethod(barPayConfig.getOtherMethod());
        payParam.setAuthCode(param.getAuthCode());
        payParam.setTerminalNo(param.getTerminalNo());
        payParam.setAppId(barPayConfig.getAppId());
        payParam.setMchNo(barPayConfig.getMchNo());
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }
        // 发起支付
        return payService.pay(payParam);
    }

    /**
     * 生成网关支付所需授权链接跳转链接, 主要是微信类通道使用, 用于获取OpenId
     */
    @IgnoreTenant
    public String genAuthUrl(GatewayAuthUrlParam param){
        // 订单信息
        var payOrder = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合类型
        var gatewayConfig = gatewayPayConfigService.getGatewayConfig(payOrder.getAppId());
        var aggregateConfig = aggregateConfigService.getAggregatePayConfig(payOrder.getAppId(), param.getAggregateType(),gatewayConfig);
        paymentAssistService.initMchAndApp(aggregateConfig.getAppId());
        // 获取认证策略
        var channelAuthStrategy = PaymentStrategyFactory.create(aggregateConfig.getChannel(), AbsChannelAuthStrategy.class);
        var aggregatePayType = AggregatePayTypeEnum.findByCode(aggregateConfig.getAggregateType());
        return switch (aggregatePayType) {
            case WECHAT -> {
                var authUrlParam = new GenerateAuthUrlParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthPath(StrUtil.format("/aggregate/wechat/{}", param.getOrderNo()));
                yield channelAuthStrategy.generateAuthUrl(authUrlParam).getAuthUrl();
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }

    /**
     * 网关支付授权结果
     */
    @IgnoreTenant
    public AuthResult auth(GatewayAuthCodeParam param) {
        // 订单信息
        var payOrder = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合类型
        var gatewayConfig = gatewayPayConfigService.getGatewayConfig(payOrder.getAppId());
        var aggregateConfig = aggregateConfigService.getAggregatePayConfig(payOrder.getAppId(), param.getAggregateType(),gatewayConfig);
        paymentAssistService.initMchAndApp(payOrder.getAppId());
        // 获取认证策略
        var channelAuthStrategy = PaymentStrategyFactory.create(aggregateConfig.getChannel(), AbsChannelAuthStrategy.class);
        var aggregatePayType = AggregatePayTypeEnum.findByCode(aggregateConfig.getAggregateType());
        return switch (aggregatePayType) {
            case WECHAT -> {
                var codeParam = new AuthCodeParam()
                        .setAuthType(ChannelAuthTypeEnum.WECHAT.getCode())
                        .setAuthCode(param.getAuthCode());
                yield channelAuthStrategy.doAuth(codeParam);
            }
            case ALIPAY, UNION_PAY -> throw new UnsupportedAbilityException("暂不支持该认证方式");
        };
    }
}
