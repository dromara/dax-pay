package cn.daxpay.open.channel.epaylinks.service.payment.pay;

import cn.daxpay.open.channel.epaylinks.client.EpaylinksChannelClient;
import cn.daxpay.open.channel.epaylinks.client.credential.EpaylinksSdkCredential;
import cn.daxpay.open.channel.epaylinks.client.enums.EpaylinksPayMethod;
import cn.daxpay.open.channel.epaylinks.client.req.EpaylinksPayReq;
import cn.daxpay.open.channel.epaylinks.client.resp.EpaylinksPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 易票联支付执行业务服务
///
/// 通过 [EpaylinksChannelClient] 调用子应用 dax-pay-channel-two 完成易票联 EFPS 网关下单。
/// 易票联为聚合通道: 微信/支付宝/云闪付主扫、微信公众号/小程序 JSAPI、
/// 支付宝生活号 JSAPI、个人网银收银台跳转。
///
/// openId 校验在子应用 fail-fast; 本类仅做平台支付方式白名单映射。
@Slf4j
@Service
@RequiredArgsConstructor
public class EpaylinksPayService {

    private final EpaylinksChannelClient epaylinksChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行易票联支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, EpaylinksSdkCredential credential) {
        // 平台支付方式 → 易票联支付方式(白名单校验)
        EpaylinksPayMethod payMethod = this.mapMethod(payParam.getMethod());

        // 构建请求
        EpaylinksPayReq req = new EpaylinksPayReq();
        // 使用支付交易号作为商户订单号透传给易票联(outTradeNo), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setPayMethod(payMethod);
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        // JSAPI/MINI 场景的用户标识(微信 openid / 支付宝买家用户ID)
        req.setOpenId(payParam.getOpenId());
        // 通道应用 AppId(微信 JSAPI/小程序上送 appId)
        req.setChannelAppId(payParam.getChannelAppId());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<EpaylinksPayResp> result = epaylinksChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 易票联支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.epaylinks.payFailed", result.getMsg());
        }

        return this.toPayResult(result.getData());
    }

    /// 生成易票联支付异步通知地址(易票联→平台, notifyUrl)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/epaylinks/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/epaylinks/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 易票联支付方式(白名单映射)
    private static EpaylinksPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 扫码族: 微信 / 支付宝 / 云闪付
            case WECHAT_QR -> EpaylinksPayMethod.WECHAT_NATIVE;
            case ALIPAY_QR -> EpaylinksPayMethod.ALIPAY_NATIVE;
            case UNION_QR -> EpaylinksPayMethod.UNION_NATIVE;
            // JSAPI 族: 微信公众号 / 微信小程序 / 支付宝生活号
            case WECHAT_JSAPI -> EpaylinksPayMethod.WECHAT_JSAPI;
            case WECHAT_MINI -> EpaylinksPayMethod.WECHAT_APPLET;
            case ALIPAY_JSAPI -> EpaylinksPayMethod.ALIPAY_JSAPI;
            // 个人网银(统一收银台跳转, 承载网银渠道)
            case UNION_H5 -> EpaylinksPayMethod.WEB_BANK_CASHIER;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.epaylinks.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(EpaylinksPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 易票联下单响应不携带平台交易号(transactionNo 经回调/查单补充)
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 EpaylinksPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
