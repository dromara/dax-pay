package cn.daxpay.open.channel.sand.service.payment.pay;

import cn.daxpay.open.channel.sand.client.SandChannelClient;
import cn.daxpay.open.channel.sand.client.credential.SandSdkCredential;
import cn.daxpay.open.channel.sand.client.enums.SandPayMethod;
import cn.daxpay.open.channel.sand.client.req.SandPayReq;
import cn.daxpay.open.channel.sand.client.resp.SandPayResp;
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

/// # 杉德支付执行业务服务
///
/// 通过 [SandChannelClient] 调用子应用 dax-pay-channel-two 完成杉德 v4 统一下单。
/// 杉德扫码为分渠道二维码(payType 区分微信/支付宝/云闪付, 非一码多渠道聚合),
/// JSAPI/小程序经 payerInfo 上送用户标识。
@Slf4j
@Service
@RequiredArgsConstructor
public class SandPayService {

    private final SandChannelClient sandChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行杉德支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, SandSdkCredential credential) {
        // 构建请求
        SandPayReq req = new SandPayReq();
        // 使用支付交易号作为商户订单号透传给杉德(outOrderNo), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setMethod(this.mapMethod(payParam.getMethod()));
        // JSAPI/MINI 场景的用户标识(微信 openid / 支付宝 buyerId)与通道应用 AppId
        req.setOpenId(payParam.getOpenId());
        req.setChannelAppId(payParam.getChannelAppId());
        req.setClientIp(payParam.getClientIp());
        req.setReturnUrl(payParam.getReturnUrl());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<SandPayResp> result = sandChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.sand.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成杉德支付异步通知地址(杉德→平台, notifyUrl)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/sand/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/sand/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 杉德支付方式
    ///
    /// 杉德扫码为分渠道二维码(微信/支付宝/云闪付各出各码);
    /// JSAPI(微信/支付宝)与小程序需随单上送用户标识; 其余方式一期未接。
    private static SandPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case WECHAT_QR -> SandPayMethod.WECHAT_QR;
            case ALIPAY_QR -> SandPayMethod.ALIPAY_QR;
            case UNION_QR -> SandPayMethod.UNION_QR;
            case WECHAT_JSAPI -> SandPayMethod.WECHAT_JSAPI;
            case WECHAT_MINI -> SandPayMethod.WECHAT_MINI;
            case ALIPAY_JSAPI -> SandPayMethod.ALIPAY_JSAPI;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.sand.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(SandPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 杉德订单流水号(sandSerialNo), 供后续查单/退款
                .setOutOrderNo(resp.getTradeNo())
                // 扫码/JSAPI 下单均为异步受理, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 SandPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }
        return bo;
    }
}
