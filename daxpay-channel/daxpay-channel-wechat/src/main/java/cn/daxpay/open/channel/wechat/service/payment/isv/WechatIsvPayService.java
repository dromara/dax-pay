package cn.daxpay.open.channel.wechat.service.payment.isv;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.enums.WechatPayBodyType;
import cn.daxpay.open.channel.wechat.client.enums.WechatPayMethod;
import cn.daxpay.open.channel.wechat.client.req.WechatPayReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
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

/// # 微信服务商支付执行业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 完成微信服务商支付下单。
/// 与直连模式([cn.daxpay.open.channel.wechat.service.payment.pay.WechatPayService])的核心差异:
/// - 调用服务商端点 `/channel/wechat/isv/pay`(子应用内部走 `/v3/partner/transactions/*`)
/// - 异步通知回调路径用 `/wechat/isv/pay`(服务商用服务商 apiV3Key 解密)
///
/// 请求构建、响应解析与直连模式一致(子应用返回结构相同)。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvPayService {

    private final WechatChannelClient wechatChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行微信服务商支付
    ///
    /// @param order      支付订单(tradeNo 作为 out_trade_no)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(服务商模式, 含 sp_mchid/sub_mchid/sp_appid/sub_appid)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, WechatSdkCredential credential) {
        // 构建请求(与直连一致)
        WechatPayReq req = new WechatPayReq();
        // 使用支付交易号作为商户订单号透传给微信, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setDescription(payParam.getTitle());
        req.setMethod(mapMethod(payParam.getMethod()));
        req.setAuthCode(payParam.getAuthCode());
        req.setOpenId(payParam.getOpenId());
        req.setAttach(payParam.getAttach());
        req.setNotifyUrl(this.buildNotifyUrl(order));
        // 关单时间取自订单(createOrder 已对 null 兜底默认30分钟), 不用 payParam 原始入参
        req.setExpireTime(order.getExpiredTime());
        if (req.getMethod() == WechatPayMethod.H5) {
            req.setWapUrl(payParam.getReturnUrl());
            req.setWapName(payParam.getTitle());
        }
        req.setCredential(credential);

        // 调用子应用服务商端点
        DaxResult<WechatPayResp> result = wechatChannelClient.isvPay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.wechat.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成微信服务商支付异步通知地址(微信→平台)
    ///
    /// 服务商回调路径: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/wechat/isv/pay`
    /// (与直连 `/wechat/pay` 分离, 服务商回调用服务商 apiV3Key 解密)
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // backendBaseUrl 未配置时抛清晰异常
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/wechat/isv/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) -> 微信通道支付方式
    private static WechatPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case WECHAT_QR -> WechatPayMethod.NATIVE;
            case WECHAT_JSAPI -> WechatPayMethod.JSAPI;
            case WECHAT_MINI -> WechatPayMethod.MINI;
            case WECHAT_APP -> WechatPayMethod.APP;
            case WECHAT_H5 -> WechatPayMethod.H5;
            case WECHAT_BARCODE -> WechatPayMethod.MICROPAY;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.wechat.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(WechatPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTransactionId())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 WechatPayBodyType -> 平台 PayBodyTypeEnum)
        WechatPayBodyType bodyType = resp.getPayBodyType();
        if (bodyType != null) {
            switch (bodyType) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case APP_ORDER_STR, IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 完成时间(MICROPAY 同步成功时返回)
        bo.setFinishTime(resp.getFinishTime());
        // 金额(MICROPAY 同步成功时返回)
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getPayerTotal());
        bo.setBuyerPayAmount(resp.getPayerTotal());
        // 用户标识
        bo.setBuyerId(resp.getOpenId());
        return bo;
    }
}
