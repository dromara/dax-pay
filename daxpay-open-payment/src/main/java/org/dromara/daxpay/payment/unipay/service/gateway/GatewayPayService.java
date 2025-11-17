package org.dromara.daxpay.payment.unipay.service.gateway;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.payment.common.context.PaymentReqInfoLocal;
import org.dromara.daxpay.payment.common.exception.UnsupportedAbilityException;
import org.dromara.daxpay.payment.common.util.PayUtil;
import org.dromara.daxpay.payment.merchant.service.gateway.AggregateConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayConfigService;
import org.dromara.daxpay.payment.merchant.service.gateway.GatewayPayReadConfigService;
import org.dromara.daxpay.payment.pay.enums.PayMethodEnum;
import org.dromara.daxpay.payment.pay.exception.TradeProcessingException;
import org.dromara.daxpay.payment.pay.local.PaymentContextLocal;
import org.dromara.daxpay.payment.pay.result.gateway.GatewayPayUrlResult;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PayAssistService;
import org.dromara.daxpay.payment.pay.service.trade.pay.PayService;
import org.dromara.daxpay.payment.pay.strategy.AbsGatewayPayStrategy;
import org.dromara.daxpay.payment.pay.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.unipay.enums.CashierSceneEnum;
import org.dromara.daxpay.payment.unipay.enums.GatewayPayTypeEnum;
import org.dromara.daxpay.payment.unipay.param.gateway.AggregateBarPayParam;
import org.dromara.daxpay.payment.unipay.param.gateway.AggregateQrPayParam;
import org.dromara.daxpay.payment.unipay.param.gateway.GatewayPayParam;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PayResult;
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
    private final PayService payService;
    private final PayAssistService payAssistService;
    private final AggregateConfigService aggregateConfigService;
    private final GatewayPayConfigService gatewayPayConfigService;
    private final PaymentAssistService paymentAssistService;
    private final GatewayPayReadConfigService gatewayPayReadConfigService;

    /**
     * 预支付: 生成网关支付链接(收银台/聚合)
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
     * 网关聚合扫码支付
     */
    @IgnoreTenant
    public PayResult aggregateQrPay(AggregateQrPayParam param){
        // 订单信息
        var payOrder = gatewayPayAssistService.getOrderAndCheck(param.getOrderNo());
        // 获取聚合扫码获取配置
        var readConfig = gatewayPayReadConfigService.findByAppId(payOrder.getAppId());
        var qrPayConfig = aggregateConfigService.getQrPayConfig(payOrder.getAppId(), readConfig);

        // 支付参数
        var payParam = new PayParam();
        payParam.setAppId(payOrder.getAppId());
        payParam.setMchNo(payOrder.getMchNo());
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }

        // 识别支付场景
        var sceneEnum = CashierSceneEnum.findByCode(param.getScene());
        // 根据类型选择对应的处理方式
        switch (sceneEnum) {
            case WECHAT_PAY -> {
                // 判断支付方式是否配置
                var payMethod = qrPayConfig.getWxMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = PayMethodEnum.BARCODE.getCode();
                }
                var gatewayPayStrategy = PaymentStrategyFactory.create(qrPayConfig.getWxChannel(), AbsGatewayPayStrategy.class);

                payParam.setChannel(qrPayConfig.getWxChannel());
                payParam.setMethod(payMethod);
                gatewayPayStrategy.handlePayParam(param, payParam);
            }
            case ALIPAY -> {
                // 判断支付方式是否配置
                var payMethod = qrPayConfig.getAlipayMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = PayMethodEnum.BARCODE.getCode();
                }
                payParam.setChannel(qrPayConfig.getWxChannel());
                payParam.setMethod(payMethod);

            }
            case UNION_PAY -> {
                // 判断支付方式是否配置
                var payMethod = qrPayConfig.getUnionMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = PayMethodEnum.BARCODE.getCode();
                }
                payParam.setChannel(qrPayConfig.getWxChannel());
                payParam.setMethod(payMethod);
            }
        }
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }
        // 发起支付, 提前进行商户应用的初始化
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        return payService.payHandle(payParam,payOrder);
    }


    /**
     * 网关聚合支付(付款码)
     */
    public PayResult aggregateBarPay(AggregateBarPayParam param){
        // 获取聚合付款码支付配置
        var readConfig = gatewayPayReadConfigService.findByAppId(param.getAppId());
        var barPayConfig = aggregateConfigService.getBarPayConfig(param.getAppId(), readConfig);
        // 支付参数
        var payParam = new PayParam();
        payParam.setAuthCode(param.getAuthCode());
        payParam.setAppId(param.getAppId());
        payParam.setMchNo(param.getMchNo());
        payParam.setTerminalNo(param.getTerminalNo());
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }

        // 识别付款码类型
        var barCodeType = PayUtil.getBarCodeType(param.getAuthCode());
        // 根据类型选择对应的处理方式
        switch (barCodeType) {
            case WECHAT_PAY -> {
                // 判断支付方式是否配置
                var payMethod = barPayConfig.getWxMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = PayMethodEnum.BARCODE.getCode();
                }
                payParam.setChannel(barPayConfig.getWxChannel());
                payParam.setMethod(payMethod);
            }
            case ALIPAY -> {
                // 判断支付方式是否配置
                var payMethod = barPayConfig.getAlipayMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = PayMethodEnum.BARCODE.getCode();
                }
                payParam.setChannel(barPayConfig.getWxChannel());
                payParam.setMethod(payMethod);

            }
            case UNION_PAY -> {
                // 判断支付方式是否配置
                var payMethod = barPayConfig.getUnionMethod();
                if (StrUtil.isBlank(payMethod)){
                    payMethod = PayMethodEnum.BARCODE.getCode();
                }
                payParam.setChannel(barPayConfig.getWxChannel());
                payParam.setMethod(payMethod);
            }
        }
        // 设置IP
        if (Objects.isNull(payParam.getClientIp())){
            String ip = Optional.ofNullable(WebServletUtil.getRequest())
                    .map(JakartaServletUtil::getClientIP)
                    .orElse("127.0.0.1");
            payParam.setClientIp(ip);
        }
        // 发起支付, 调用钱已经进行商户应用的初始化, 不需要重新初始化了
        return payService.pay(payParam);
    }

}
