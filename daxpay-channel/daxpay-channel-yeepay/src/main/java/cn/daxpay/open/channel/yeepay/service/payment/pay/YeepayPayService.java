package cn.daxpay.open.channel.yeepay.service.payment.pay;

import cn.daxpay.open.channel.yeepay.client.YeepayChannelClient;
import cn.daxpay.open.channel.yeepay.client.credential.YeepaySdkCredential;
import cn.daxpay.open.channel.yeepay.client.enums.YeepayPayMethod;
import cn.daxpay.open.channel.yeepay.client.req.YeepayPayReq;
import cn.daxpay.open.channel.yeepay.client.resp.YeepayPayResp;
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

/// # 易宝支付执行业务服务
///
/// 通过 [YeepayChannelClient] 调用子应用 dax-pay-channel-two 完成易宝聚合支付下单。
/// 易宝聚合通道: 一个应用承载微信/支付宝/银联三种底层渠道, 由 [YeepayPayMethod] 决定调用方式。
@Slf4j
@Service
@RequiredArgsConstructor
public class YeepayPayService {

    private final YeepayChannelClient yeepayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行易宝支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, YeepaySdkCredential credential) {
        // 构建请求
        YeepayPayReq req = new YeepayPayReq();
        // 使用支付交易号作为商户订单号透传给易宝, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(this.mapMethod(payParam.getMethod()));
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<YeepayPayResp> result = yeepayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.yeepayPayFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成易宝支付异步通知地址(易宝→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/yeepay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/yeepay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 易宝支付方式
    private static YeepayPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 聚合扫码(不指定渠道)
            case AGGREGATE_PAY_QRCODE -> YeepayPayMethod.QRCODE;
            // 微信
            case WECHAT_QR -> YeepayPayMethod.WECHAT_QR;
            case WECHAT_H5 -> YeepayPayMethod.WECHAT_H5;
            // 支付宝
            case ALIPAY_QR -> YeepayPayMethod.ALIPAY_QR;
            case ALIPAY_H5, ALIPAY_PC -> YeepayPayMethod.ALIPAY_H5;
            // 银联
            case UNION_QR -> YeepayPayMethod.UNION_QR;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.yeepayUnsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(YeepayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                // 扫码/H5 均为异步支付, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 YeepayPayBodyType → 平台 PayBodyTypeEnum)
        if (resp.getPayBodyType() != null) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
