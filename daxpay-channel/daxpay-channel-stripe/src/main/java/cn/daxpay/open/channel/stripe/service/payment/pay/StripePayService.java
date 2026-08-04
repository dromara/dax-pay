package cn.daxpay.open.channel.stripe.service.payment.pay;

import cn.daxpay.open.channel.stripe.client.StripeChannelClient;
import cn.daxpay.open.channel.stripe.client.credential.StripeSdkCredential;
import cn.daxpay.open.channel.stripe.client.enums.StripePayBodyType;
import cn.daxpay.open.channel.stripe.client.req.StripePayReq;
import cn.daxpay.open.channel.stripe.client.resp.StripePayResp;
import cn.daxpay.open.channel.stripe.code.StripePayCode;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/// # Stripe 支付执行业务服务
///
/// 通过 [StripeChannelClient] 调用子应用 dax-pay-channel-three 完成 Stripe 支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 双模式选择(按平台支付方式):
/// - VISA_CARD_GATEWAY → PaymentIntent + Elements(自嵌卡组件, 前端调 confirmCardPayment)
/// - MASTERCARD_CARD_GATEWAY → Checkout Session(Stripe 托管收银台, 跳转)
@Slf4j
@Service
@RequiredArgsConstructor
public class StripePayService {

    private final StripeChannelClient stripeChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行 Stripe 支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, StripeSdkCredential credential) {
        // 构建请求
        StripePayReq req = new StripePayReq();
        // 使用平台资金交易号作为 metadata.orderNo, 回调时凭此反查 PayTrade
        req.setOrderNo(order.getTradeNo());
        req.setBizOrderNo(payParam.getBizOrderNo());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        // 金额(最小货币单位, Stripe 同单位直接透传) + 币种
        req.setAmount(payParam.getAmount());
        req.setCurrency(payParam.getCurrency());
        // 支付方式: 由平台支付方式决定 Stripe 接入模式
        req.setMethod(this.mapMethod(payParam.getMethod()));
        // 同步跳转地址(Checkout Session 模式必填)
        req.setReturnUrl(payParam.getReturnUrl());
        // 通道通知地址: 平台生成的回调地址(Stripe→平台), 带 channelMchNo 供回调组装凭证验签
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setAttach(payParam.getAttach());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<StripePayResp> result = stripeChannelClient.pay(req);
        if (result.getCode() != 0) {
            // Stripe 支付失败
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.stripe.payFailed", result.getMsg());
        }

        return toPayResult(result.getData(), credential, req.getReturnUrl());
    }

    /// 生成 Stripe 支付异步通知地址(Stripe→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/stripe/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 平台后端访问地址未配置, 无法生成 Stripe 回调地址
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成 Stripe 回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/stripe/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → Stripe 接入模式
    private static String mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // Visa 网关: PaymentIntent + Elements 模式
            case VISA_CARD_GATEWAY -> StripePayCode.PAY_METHOD_INTENT;
            // 万事达网关: Checkout Session 跳转模式
            case MASTERCARD_CARD_GATEWAY -> StripePayCode.PAY_METHOD_CHECKOUT;
            // Stripe 不支持该支付方式
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.stripe.notSupportMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO(透传 Stripe 类型, 前端按 payBodyType 分发)
    private PayTradeResultBo toPayResult(StripePayResp resp, StripeSdkCredential credential, String returnUrl) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setPayBody(resp.getPayBody())
                // 通道侧订单号(PaymentIntent ID / Session ID)
                .setOutOrderNo(resp.getOutOrderNo());
        // 支付内容类型映射(子应用 StripePayBodyType -> 平台 PayBodyTypeEnum)
        StripePayBodyType bodyType = StripePayBodyType.valueOf(resp.getPayBodyType());
        if (bodyType != null) {
            switch (bodyType) {
                // Checkout Session: 跳转 URL(前端 redirect 分支)
                case CHECKOUT_URL -> bo.setPayBodyType(PayBodyTypeEnum.STRIPE_CHECKOUT);
                // PaymentIntent: payBody 组装为 JSON(clientSecret + publishableKey + returnUrl),
                // 前端 Stripe.js Elements 面板解析后调 confirmCardPayment
                case INTENT_SECRET -> {
                    bo.setPayBodyType(PayBodyTypeEnum.STRIPE_INTENT);
                    bo.setPayBody(buildIntentPayBody(resp.getPayBody(), credential, returnUrl));
                }
            }
        }
        return bo;
    }

    /// 组装 PaymentIntent 模式的支付参数体(JSON 字符串)
    ///
    /// 前端面板需要 clientSecret 调 confirmCardPayment、publishableKey 初始化 Stripe、
    /// returnUrl 供 3DS 完成后的同步跳转兜底。
    private String buildIntentPayBody(String clientSecret, StripeSdkCredential credential, String returnUrl) {
        Map<String, String> payload = new HashMap<>();
        payload.put("clientSecret", clientSecret);
        payload.put("publishableKey", credential.getPublishableKey());
        if (StrUtil.isNotBlank(returnUrl)) {
            payload.put("returnUrl", returnUrl);
        }
        return JacksonUtil.toJson(payload);
    }
}
