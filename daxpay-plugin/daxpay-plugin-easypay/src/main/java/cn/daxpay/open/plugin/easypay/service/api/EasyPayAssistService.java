package cn.daxpay.open.plugin.easypay.service.api;

import cn.daxpay.open.payment.common.context.MerchantContextLoader;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeSourceEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.exception.ValidationFailedException;
import cn.daxpay.open.plugin.easypay.dao.EasyPayOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayCredential;
import cn.daxpay.open.plugin.easypay.entity.EasyPayOrder;
import cn.daxpay.open.plugin.easypay.enums.EasyPayMethodEnum;
import cn.daxpay.open.plugin.easypay.result.api.EasyPayOrderStatusResult;
import cn.daxpay.open.plugin.easypay.result.api.EasyPaySubmitInfoResult;
import cn.daxpay.open.plugin.easypay.util.EasyPayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 易支付通用辅助
///
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayAssistService {

    private final EasyPayOrderManager easyPayOrderManager;
    private final MerchantContextLoader merchantContextLoader;
    private final NormalPayOrderManager normalPayOrderManager;
    private final PayTradeManager payTradeManager;

    /// V2 RSA 验签并校验版本开关
    public EasyPayCredential checkSignV2(Object param, EasyPayCredential credential, String sign) {
        if (!credential.getEnableV2()) {
            throw new ValidationFailedException("error.plugin.easypay.v2Disabled");
        }
        boolean ok;
        if (credential.getUseSystemKey()) {
            // 商户用平台公钥验签？不对——请求是商户私钥签，平台用商户公钥验
            // useSystemKey 时：商户用平台提供的密钥对中的私钥签名？商业版逻辑：useSystemKey 用系统密钥
            // 商业：请求验签用商户公钥；useSystemKey 时平台私钥签响应，验请求用商户 publicKey
            String publicKey = StrUtil.blankToDefault(credential.getPublicKey(), credential.getPlatformPublicKey());
            ok = EasyPayUtil.verifySignByRsa(param, sign, publicKey);
        } else {
            if (StrUtil.isBlank(credential.getPublicKey())) {
                throw new ValidationFailedException("error.plugin.easypay.publicKeyMissing");
            }
            ok = EasyPayUtil.verifySignByRsa(param, sign, credential.getPublicKey());
        }
        if (!ok) {
            throw new ValidationFailedException("error.plugin.easypay.signInvalid");
        }
        merchantContextLoader.initMch(credential.getMchNo());
        return credential;
    }

    /// V1 MD5 验签
    public EasyPayCredential checkSignV1(Object param, EasyPayCredential credential, String sign) {
        if (!credential.getEnableV1()) {
            throw new ValidationFailedException("error.plugin.easypay.v1Disabled");
        }
        if (StrUtil.isBlank(credential.getMd5Key())) {
            throw new ValidationFailedException("error.plugin.easypay.md5KeyMissing");
        }
        if (!EasyPayUtil.verifySignByMd5(param, sign, credential.getMd5Key())) {
            throw new ValidationFailedException("error.plugin.easypay.signInvalid");
        }
        merchantContextLoader.initMch(credential.getMchNo());
        return credential;
    }

    /// 响应 RSA 签名用私钥
    public String responsePrivateKey(EasyPayCredential credential) {
        return credential.getPlatformPrivateKey();
    }

    /// 协议 type + create method 映射内部 PayMethod
    public String resolvePayMethod(String type, String method, String device, Integer isApplet) {
        EasyPayMethodEnum methodEnum = EasyPayMethodEnum.findByCode(type);
        if (methodEnum == null) {
            throw new ValidationFailedException("error.plugin.easypay.unsupportedType");
        }
        boolean applet = Objects.equals(isApplet, 1);
        boolean mobile = StrUtil.equalsIgnoreCase(device, "mobile");
        return switch (methodEnum) {
            case ALIPAY -> switch (StrUtil.blankToDefault(method, "web")) {
                case "web" -> PayMethodEnum.ALIPAY_QR.getCode();
                case "jump" -> mobile ? PayMethodEnum.ALIPAY_H5.getCode() : PayMethodEnum.ALIPAY_PC.getCode();
                case "jsapi" -> PayMethodEnum.ALIPAY_JSAPI.getCode();
                default -> throw new ValidationFailedException("error.plugin.easypay.unsupportedMethod");
            };
            case WECHAT -> switch (StrUtil.blankToDefault(method, "web")) {
                case "web" -> PayMethodEnum.WECHAT_QR.getCode();
                case "jump" -> PayMethodEnum.WECHAT_H5.getCode();
                case "jsapi" -> applet ? PayMethodEnum.WECHAT_MINI.getCode() : PayMethodEnum.WECHAT_JSAPI.getCode();
                default -> throw new ValidationFailedException("error.plugin.easypay.unsupportedMethod");
            };
            case AGGREGATE -> PayMethodEnum.AGGREGATE_PAY_QRCODE.getCode();
        };
    }

    /// 是否需要收银台（jsapi / 聚合）
    public boolean needCashier(String payMethod) {
        return Objects.equals(payMethod, PayMethodEnum.WECHAT_JSAPI.getCode())
                || Objects.equals(payMethod, PayMethodEnum.WECHAT_MINI.getCode())
                || Objects.equals(payMethod, PayMethodEnum.ALIPAY_JSAPI.getCode())
                || Objects.equals(payMethod, PayMethodEnum.AGGREGATE_PAY_QRCODE.getCode());
    }

    /// payBodyType → 协议 pay_type
    public String toPayType(String payBodyType) {
        if (payBodyType == null) {
            return "qrcode";
        }
        if (Objects.equals(payBodyType, PayBodyTypeEnum.QR_CODE.getCode())
                || Objects.equals(payBodyType, PayBodyTypeEnum.LINK.getCode())) {
            return "qrcode";
        }
        if (Objects.equals(payBodyType, PayBodyTypeEnum.JSAPI.getCode())) {
            return "jsapi";
        }
        if (Objects.equals(payBodyType, PayBodyTypeEnum.FROM.getCode())) {
            return "html";
        }
        return "qrcode";
    }

    /// 查询协议订单状态（内部轮询）
    public EasyPayOrderStatusResult queryOrderStatus(Long id) {
        var order = easyPayOrderManager.findByIdNotTenant(id)
                .orElseThrow(() -> new DataNotExistException("error.plugin.easypay.orderNotFound"));
        return new EasyPayOrderStatusResult()
                .setStatus(order.getStatus())
                .setReturnUrl(order.getReturnUrl())
                .setTradeNo(order.getTradeNo())
                .setOutTradeNo(order.getOutTradeNo());
    }

    /// 查询收银台订单信息（内部）
    public EasyPaySubmitInfoResult findSubmitInfo(Long id) {
        var order = easyPayOrderManager.findByIdNotTenant(id)
                .orElseThrow(() -> new DataNotExistException("error.plugin.easypay.orderNotFound"));
        return new EasyPaySubmitInfoResult()
                .setId(order.getId())
                .setType(order.getType())
                .setName(order.getName())
                .setMoney(order.getMoney())
                .setPayUrl(order.getPayUrl())
                .setPayBody(order.getPayBody())
                .setPcCallType(order.getPcCallType())
                .setStatus(order.getStatus())
                .setReturnUrl(order.getReturnUrl());
    }

    /// 根据协议单定位资金交易
    public PayTrade requireTrade(EasyPayOrder order) {
        if (order.getOrderId() != null) {
            return payTradeManager.findByContainerId(order.getOrderId(), PayTradeTypeEnum.NORMAL.getCode())
                    .orElseThrow(() -> new DataNotExistException("error.plugin.easypay.tradeNotFound"));
        }
        throw new DataNotExistException("error.plugin.easypay.tradeNotFound");
    }

    /// 交易来源编码（易支付协议）
    public String sourceCode() {
        return TradeSourceEnum.EASY_PAY.getCode();
    }
}
