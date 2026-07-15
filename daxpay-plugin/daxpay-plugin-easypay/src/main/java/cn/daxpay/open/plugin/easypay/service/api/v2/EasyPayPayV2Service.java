package cn.daxpay.open.plugin.easypay.service.api.v2;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.trade.runtime.service.pay.normal.NormalPayService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPayResult;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.enums.EasyPayApiVersionEnum;
import cn.daxpay.open.plugin.easypay.enums.EasyPayMethodEnum;
import cn.daxpay.open.plugin.easypay.param.api.v1.EasyPayH5PayParam;
import cn.daxpay.open.plugin.easypay.param.api.v2.EasyPayCreateV2Param;
import cn.daxpay.open.plugin.easypay.param.api.v2.EasyPaySubmitV2Param;
import cn.daxpay.open.plugin.easypay.result.api.v2.EasyPayCreateV2Result;
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
import java.util.Objects;

/// # 易支付 V2 支付服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPayV2Service {

    private final EasyPayCredentialService easyPayCredentialService;
    private final EasyPayAssistService easyPayAssistService;
    private final EasyPayOrderManager easyPayOrderManager;
    private final NormalPayService normalPayService;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final MerchantContextLoader merchantContextLoader;

    /// 页面跳转支付 → 收银台 URL
    public String submit(EasyPaySubmitV2Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV2(param, credential, param.getSign());
        String gateway = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        var existing = easyPayOrderManager.findByOutTradeNo(param.getOutTradeNo());
        if (existing.isPresent()) {
            return gateway + "/easypay/v2/pay/" + existing.get().getId();
        }
        var order = buildOrder(param.getOutTradeNo(), param.getType(), param.getName(), param.getMoney(),
                param.getNotifyUrl(), param.getReturnUrl(), param.getParam(), param.getClientip(),
                credential, EasyPayApiVersionEnum.V2.getCode());
        order.setPayUrl(gateway + "/easypay/v2/pay/" + order.getId())
                .setPcCallType(PayBodyTypeEnum.QR_CODE.getCode());
        easyPayOrderManager.save(order);
        return order.getPayUrl();
    }

    /// 统一下单
    public EasyPayCreateV2Result create(EasyPayCreateV2Param param) {
        var credential = easyPayCredentialService.getAndCheck(param.getPid());
        easyPayAssistService.checkSignV2(param, credential, param.getSign());
        EasyPayCreateV2Result result = new EasyPayCreateV2Result();
        if (!StrUtil.equalsAny(param.getMethod(), "web", "jump", "jsapi")) {
            return fail(result, credential, "不支持的接口类型: " + param.getMethod());
        }
        if (!StrUtil.equalsAny(param.getType(), EasyPayMethodEnum.ALIPAY.getCode(), EasyPayMethodEnum.WECHAT.getCode())) {
            return fail(result, credential, "不支持的支付方式: " + param.getType());
        }

        EasyPayOrder order;
        var optional = easyPayOrderManager.findByOutTradeNo(param.getOutTradeNo());
        if (optional.isPresent()) {
            order = optional.get();
            if (StrUtil.isNotBlank(order.getPayBody()) || StrUtil.isNotBlank(order.getPayUrl())) {
                return successFromOrder(result, credential, order);
            }
        } else {
            order = buildOrder(param.getOutTradeNo(), param.getType(), param.getName(), param.getMoney(),
                    param.getNotifyUrl(), param.getReturnUrl(), param.getParam(),
                    StrUtil.blankToDefault(param.getClientip(), WebServletUtil.getClientIp()),
                    credential, EasyPayApiVersionEnum.V2.getCode());
            easyPayOrderManager.save(order);
        }

        String payMethod = easyPayAssistService.resolvePayMethod(
                param.getType(), param.getMethod(), param.getDevice(), param.getIsApplet());
        String gateway = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();

        // 需要 openId 且未传时，先回收银台
        if (easyPayAssistService.needCashier(payMethod) && StrUtil.isBlank(param.getSubOpenid())) {
            order.setPayUrl(gateway + "/easypay/v2/h5/" + order.getId())
                    .setPcCallType(PayBodyTypeEnum.QR_CODE.getCode());
            easyPayOrderManager.updateById(order);
            return successFromOrder(result, credential, order);
        }

        try {
            NormalPayResult payResult = doNormalPay(order, credential, payMethod, param.getSubOpenid(), param.getAuthCode());
            fillOrderFromPayResult(order, payResult);
            easyPayOrderManager.updateById(order);
            result.setCode(0)
                    .setMsg("success")
                    .setTradeNo(order.getTradeNo())
                    .setPayType(easyPayAssistService.toPayType(payResult.getPayBodyType()))
                    .setPayInfo(payResult.getPayBody())
                    .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
            result.setSign(EasyPayUtil.signByRsa(result, easyPayAssistService.responsePrivateKey(credential)));
            result.setSignType("RSA");
            return result;
        } catch (Exception e) {
            log.error("易支付V2下单失败", e);
            return fail(result, credential, e.getMessage());
        }
    }

    /// H5 收银台拉起支付（内部接口，不验签）
    public NormalPayResult submitPayByH5(EasyPayH5PayParam param) {
        var order = easyPayOrderManager.findByIdNotTenant(param.getId())
                .orElseThrow(() -> new DataNotExistException("error.plugin.easypay.orderNotFound"));
        if (StrUtil.isNotBlank(order.getPayBody())) {
            return new NormalPayResult()
                    .setPayBody(order.getPayBody())
                    .setOrderId(order.getOrderId())
                    .setOrderNo(order.getTradeNo())
                    .setBizOrderNo(order.getOutTradeNo());
        }
        var credential = easyPayCredentialService.getAndCheck(order.getPid());
        merchantContextLoader.initMch(credential.getMchNo());

        String scene = StrUtil.blankToDefault(param.getScene(), "wechat_pay");
        String payMethod;
        if (StrUtil.equalsAnyIgnoreCase(scene, "alipay", "alipay_pay")) {
            payMethod = PayMethodEnum.ALIPAY_JSAPI.getCode();
            order.setType(EasyPayMethodEnum.ALIPAY.getCode());
        } else {
            payMethod = PayMethodEnum.WECHAT_JSAPI.getCode();
            order.setType(EasyPayMethodEnum.WECHAT.getCode());
        }
        NormalPayResult payResult = doNormalPay(order, credential, payMethod, param.getOpenId(), null);
        fillOrderFromPayResult(order, payResult);
        easyPayOrderManager.updateById(order);
        return payResult;
    }

    private void fillOrderFromPayResult(EasyPayOrder order, NormalPayResult payResult) {
        order.setOrderId(payResult.getOrderId())
                .setTradeNo(payResult.getOrderNo())
                .setPayBody(payResult.getPayBody())
                .setPayUrl(payResult.getPayBody())
                .setPcCallType(payResult.getPayBodyType());
        if (Objects.equals(payResult.getStatus(), "success")) {
            order.setStatus(1).setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        }
    }

    private NormalPayResult doNormalPay(EasyPayOrder order, EasyPayCredential credential,
                                        String payMethod, String openId, String authCode) {
        NormalPayParam payParam = new NormalPayParam();
        payParam.setMchNo(credential.getMchNo());
        payParam.setAppId(credential.getAppId());
        payParam.setBizOrderNo(order.getOutTradeNo());
        payParam.setTitle(order.getName());
        payParam.setAmount(EasyPayUtil.yuanToFen(order.getMoney()));
        payParam.setMethod(payMethod);
        payParam.setOpenId(openId);
        payParam.setAuthCode(authCode);
        payParam.setClientIp(order.getClientIp());
        payParam.setNotifyUrl(order.getNotifyUrl());
        payParam.setReturnUrl(order.getReturnUrl());
        payParam.setAttach(order.getParam());
        payParam.setSource(easyPayAssistService.sourceCode());
        return normalPayService.pay(payParam);
    }

    private EasyPayOrder buildOrder(String outTradeNo, String type, String name, String money,
                                    String notifyUrl, String returnUrl, String param, String clientIp,
                                    EasyPayCredential credential, String apiVersion) {
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
        order.setApiVersion(apiVersion);
        order.setAddTime(OffsetDateTime.now(ZoneOffset.UTC));
        order.setParam(param);
        order.setClientIp(StrUtil.blankToDefault(clientIp, WebServletUtil.getClientIp()));
        order.setNotifyUrl(notifyUrl);
        order.setReturnUrl(returnUrl);
        order.setStatus(0);
        return order;
    }

    private EasyPayCreateV2Result successFromOrder(EasyPayCreateV2Result result, EasyPayCredential credential, EasyPayOrder order) {
        result.setCode(0)
                .setMsg("success")
                .setTradeNo(order.getTradeNo())
                .setPayType(StrUtil.blankToDefault(easyPayAssistService.toPayType(order.getPcCallType()), "qrcode"))
                .setPayInfo(StrUtil.blankToDefault(order.getPayBody(), order.getPayUrl()))
                .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        result.setSign(EasyPayUtil.signByRsa(result, easyPayAssistService.responsePrivateKey(credential)));
        result.setSignType("RSA");
        return result;
    }

    private EasyPayCreateV2Result fail(EasyPayCreateV2Result result, EasyPayCredential credential, String msg) {
        result.setCode(-1)
                .setMsg(msg)
                .setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        try {
            result.setSign(EasyPayUtil.signByRsa(result, easyPayAssistService.responsePrivateKey(credential)));
            result.setSignType("RSA");
        } catch (Exception ignored) {
            // 签名失败不阻断错误返回
        }
        return result;
    }
}
