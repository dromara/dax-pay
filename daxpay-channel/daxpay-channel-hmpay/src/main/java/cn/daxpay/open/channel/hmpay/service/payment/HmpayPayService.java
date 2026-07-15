package cn.daxpay.open.channel.hmpay.service.payment;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.enums.HmpayPayMethod;
import cn.daxpay.open.channel.hmpay.client.req.HmpayPayReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
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

/// # 河马付服务商支付执行业务服务
///
/// 通过 [HmpayChannelClient] 调用子应用 dax-pay-channel-two 完成河马付(杉德)支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 河马付聚合通道: 一个接口承载微信/支付宝两种底层渠道 + 聚合扫码/条码, 由 [HmpayPayMethod] 决定路由。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayPayService {

    private final HmpayChannelClient hmpayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行河马付支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, HmpaySdkCredential credential) {
        // 平台支付方式 → 河马付支付方式
        HmpayPayMethod method = mapMethod(payParam.getMethod());

        // 构建请求
        HmpayPayReq req = new HmpayPayReq();
        // 使用支付交易号作为商户订单号透传给杉德(out_order_no), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(method);
        // JSAPI/MINI 场景的用户标识(微信 openid / 支付宝 buyerId)
        req.setOpenId(payParam.getOpenId());
        // 通道应用 AppId(微信 mer_app_id 等)
        req.setChannelAppId(payParam.getChannelAppId());
        req.setClientIp(payParam.getClientIp());
        // 杉德 notify_url 由子应用从 credential.notifyUrl 透传
        credential.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);
        // 条码支付需要付款码
        if (method == HmpayPayMethod.BARCODE) {
            req.setAuthCode(payParam.getAuthCode());
        }

        // 调用子应用
        DaxResult<HmpayPayResp> result = hmpayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.hmpayPayFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/hmpay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/hmpay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 河马付支付方式
    private static HmpayPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 聚合扫码(通道原生通扫码)
            case AGGREGATE_PAY_QRCODE -> HmpayPayMethod.AGGREGATE_QR;
            // 微信(扫码/JSAPI/小程序)
            case WECHAT_QR -> HmpayPayMethod.WECHAT_QR;
            case WECHAT_JSAPI -> HmpayPayMethod.WECHAT_JSAPI;
            case WECHAT_MINI -> HmpayPayMethod.WECHAT_MINI;
            // 支付宝(扫码/JSAPI/小程序)
            case ALIPAY_QR -> HmpayPayMethod.ALIPAY_QR;
            case ALIPAY_JSAPI -> HmpayPayMethod.ALIPAY_JSAPI;
            // 付款码: 平台已识别分钱包 method, 通道折叠为统一条码 API
            case WECHAT_BARCODE, ALIPAY_BARCODE -> HmpayPayMethod.BARCODE;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.hmpayUnsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(HmpayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                // 扫码/JSAPI 下单均为异步受理, 条码支付可能同步完成
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 HmpayPayBodyType → 平台 PayBodyTypeEnum)
        if (resp.getPayBodyType() != null) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }
        return bo;
    }
}
