package cn.daxpay.open.channel.dougong.service.payment;

import cn.daxpay.open.channel.dougong.client.DougongChannelClient;
import cn.daxpay.open.channel.dougong.client.credential.DougongSdkCredential;
import cn.daxpay.open.channel.dougong.client.enums.DougongPayBodyType;
import cn.daxpay.open.channel.dougong.client.enums.DougongPayMethod;
import cn.daxpay.open.channel.dougong.client.req.DougongPayReq;
import cn.daxpay.open.channel.dougong.client.resp.DougongPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 斗拱服务商支付执行业务服务
///
/// 通过 [DougongChannelClient] 调用子应用 dax-pay-channel-two 完成斗拱(汇付天下)支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 斗拱聚合通道: 一个接口承载微信/支付宝/银联三种底层渠道, 由 tradeType(隐含在 [DougongPayMethod])决定路由。
@Slf4j
@Service
@RequiredArgsConstructor
public class DougongPayService {

    private final DougongChannelClient dougongChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行斗拱支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, DougongSdkCredential credential) {
        // 平台支付方式 → 斗拱支付方式(含 tradeType + bodyType)
        DougongPayMethod method = mapMethod(payParam.getMethod());

        // 构建请求
        DougongPayReq req = new DougongPayReq();
        // 使用支付交易号作为商户订单号透传给汇付(reqSeqId), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(method);
        // JSAPI/MINI 场景的用户标识(微信 openid / 支付宝 buyerId)
        req.setOpenId(payParam.getOpenId());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order));
        req.setExpireTime(order.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<DougongPayResp> result = dougongChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.dougongPayFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成斗拱支付异步通知地址(汇付→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/dougong/pay`
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/dougong/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) → 斗拱支付方式
    private static DougongPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 微信(扫码/JSAPI/小程序)
            case WECHAT_QR -> DougongPayMethod.WECHAT_QR;
            case WECHAT_JSAPI -> DougongPayMethod.WECHAT_JSAPI;
            case WECHAT_MINI -> DougongPayMethod.WECHAT_MINI;
            // 支付宝(扫码/JSAPI/小程序)
            case ALIPAY_QR -> DougongPayMethod.ALIPAY_QR;
            case ALIPAY_JSAPI, ALIPAY_MINI -> DougongPayMethod.ALIPAY_JSAPI;
            // 银联(扫码)
            case UNION_QR -> DougongPayMethod.UNION_QR;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.dougongUnsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(DougongPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                // 斗拱下单均为异步受理, 最终状态由回调/同步确认
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 DougongPayBodyType → 平台 PayBodyTypeEnum)
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
