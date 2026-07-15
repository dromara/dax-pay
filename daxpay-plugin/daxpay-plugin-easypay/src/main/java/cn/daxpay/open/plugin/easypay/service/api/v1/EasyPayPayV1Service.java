package cn.daxpay.open.plugin.easypay.service.api.v1;

import cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.enums.EasyPayApiVersionEnum;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPayCreateV1Param;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPaySubmitV1Param;
import cn.daxpay.open.plugin.easypay.result.api.v1.EasyPayCreateV1Result;
import cn.daxpay.open.plugin.easypay.service.api.EasyPayAssistService;
import cn.daxpay.open.plugin.easypay.service.config.EasyPayCredentialService;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/// # 易支付 V1 支付
///
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPayV1Service {

    private final EasyPayCredentialService easyPayCredentialService;
    private final EasyPayAssistService easyPayAssistService;
    private final EasyPayOrderManager easyPayOrderManager;
    private final NormalPayService normalPayService;
    private final PlatformUrlConfigService platformUrlConfigService;

    public String submit(EasyPaySubmitV1Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV1(param, credential, param.getSign());
        String gateway = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        var existing = easyPayOrderManager.findByOutTradeNo(param.getOutTradeNo());
        if (existing.isPresent()) {
            return gateway + "/easypay/v1/pay/" + existing.get().getId();
        }
        var order = newOrder(param.getOutTradeNo(), param.getType(), param.getName(), param.getMoney(),
                param.getNotifyUrl(), param.getReturnUrl(), param.getParam(), param.getClientip(),
                credential);
        order.setPayUrl(gateway + "/easypay/v1/pay/" + order.getId())
                .setPcCallType(PayBodyTypeEnum.QR_CODE.getCode());
        easyPayOrderManager.save(order);
        return order.getPayUrl();
    }

    public EasyPayCreateV1Result create(EasyPayCreateV1Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV1(param, credential, param.getSign());
        EasyPayCreateV1Result result = new EasyPayCreateV1Result();
        try {
            EasyPayOrder order;
            var optional = easyPayOrderManager.findByOutTradeNo(param.getOutTradeNo());
            if (optional.isPresent()) {
                order = optional.get();
                if (StrUtil.isNotBlank(order.getPayBody()) || StrUtil.isNotBlank(order.getPayUrl())) {
                    return fillResult(result, order);
                }
            } else {
                order = newOrder(param.getOutTradeNo(), param.getType(), param.getName(), param.getMoney(),
                        param.getNotifyUrl(), param.getReturnUrl(), param.getParam(),
                        StrUtil.blankToDefault(param.getClientip(), WebServletUtil.getClientIp()),
                        credential);
                easyPayOrderManager.save(order);
            }
            String payMethod = easyPayAssistService.resolvePayMethod(param.getType(), "web", param.getDevice(), null);
            if (easyPayAssistService.needCashier(payMethod)) {
                String gateway = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
                order.setPayUrl(gateway + "/easypay/v1/h5/" + order.getId());
                easyPayOrderManager.updateById(order);
                return fillResult(result, order);
            }
            NormalPayParam payParam = new NormalPayParam();
            payParam.setMchNo(credential.getMchNo());
            payParam.setAppId(credential.getAppId());
            payParam.setBizOrderNo(order.getOutTradeNo());
            payParam.setTitle(order.getName());
            payParam.setAmount(EasyPayUtil.yuanToFen(order.getMoney()));
            payParam.setMethod(payMethod);
            payParam.setClientIp(order.getClientIp());
            payParam.setSource(easyPayAssistService.sourceCode());
            var payResult = normalPayService.pay(payParam);
            order.setOrderId(payResult.getOrderId())
                    .setTradeNo(payResult.getOrderNo())
                    .setPayBody(payResult.getPayBody())
                    .setPayUrl(payResult.getPayBody())
                    .setPcCallType(payResult.getPayBodyType());
            easyPayOrderManager.updateById(order);
            return fillResult(result, order);
        } catch (Exception e) {
            log.error("易支付V1下单失败", e);
            result.setCode(-1).setMsg(e.getMessage());
            return result;
        }
    }

    private EasyPayCreateV1Result fillResult(EasyPayCreateV1Result result, EasyPayOrder order) {
        result.setCode(0).setMsg("success").setTradeNo(order.getTradeNo());
        String body = StrUtil.blankToDefault(order.getPayBody(), order.getPayUrl());
        String callType = order.getPcCallType();
        if (ObjectsEqualsQr(callType) || StrUtil.isBlank(order.getPayBody())) {
            result.setQrcode(body);
            result.setPayurl(order.getPayUrl());
        } else {
            result.setPayurl(body);
        }
        return result;
    }

    private boolean ObjectsEqualsQr(String callType) {
        return PayBodyTypeEnum.QR_CODE.getCode().equals(callType)
                || PayBodyTypeEnum.LINK.getCode().equals(callType);
    }

    private EasyPayOrder newOrder(String outTradeNo, String type, String name, String money,
                                  String notifyUrl, String returnUrl, String param, String clientIp,
                                  cn.daxpay.open.plugin.easypay.entity.EasyPayCredential credential) {
        EasyPayOrder order = new EasyPayOrder();
        order.setId(IdUtil.getSnowflakeNextId());
        order.setOutTradeNo(outTradeNo);
        order.setType(type);
        order.setName(name);
        order.setMoney(new BigDecimal(money));
        order.setRefundMoney(BigDecimal.ZERO);
        order.setPid(credential.getPid());
        order.setAppId(credential.getAppId());
        order.setMchNo(credential.getMchNo());
        order.setApiVersion(EasyPayApiVersionEnum.V1.getCode());
        order.setAddTime(OffsetDateTime.now(ZoneOffset.UTC));
        order.setParam(param);
        order.setClientIp(StrUtil.blankToDefault(clientIp, WebServletUtil.getClientIp()));
        order.setNotifyUrl(notifyUrl);
        order.setReturnUrl(returnUrl);
        order.setStatus(0);
        return order;
    }
}
