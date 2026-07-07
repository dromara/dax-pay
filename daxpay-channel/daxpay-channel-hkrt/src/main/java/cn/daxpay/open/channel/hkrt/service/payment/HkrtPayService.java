package cn.daxpay.open.channel.hkrt.service.payment;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.enums.HkrtPayBodyType;
import cn.daxpay.open.channel.hkrt.client.enums.HkrtPayMethod;
import cn.daxpay.open.channel.hkrt.client.req.HkrtPayReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtPayResp;
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

/// # 海科融通服务商支付执行业务服务
///
/// 通过 [HkrtChannelClient] 调用子应用 dax-pay-channel-two 完成海科融通支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 海科融通聚合通道: 一个接口承载微信/支付宝/银联三种底层渠道, 由 method 单独决定路由,
/// 不像拉卡拉需要 accountType + transType 三要素。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtPayService {

    private final HkrtChannelClient hkrtChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行海科融通支付
    ///
    /// @param order      支付订单(tradeNo 作为 out_trade_no)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(服务商密钥 + 商户号/终端号)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, HkrtSdkCredential credential) {
        // 平台支付方式 → 海科融通 method + bodyType
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        HkrtPayReq req = new HkrtPayReq();
        // 使用支付交易号作为商户订单号透传给海科融通, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method);
        req.setPayBodyType(mapping.bodyType);
        // 通道专属参数透传
        req.setOpenId(payParam.getOpenId());
        req.setAuthCode(payParam.getAuthCode());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order));
        req.setExpireTime(order.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<HkrtPayResp> result = hkrtChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.hkrtPayFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成海科融通支付异步通知地址(海科融通→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/hkrt/pay`
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/hkrt/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) → 海科融通 method + bodyType
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        HkrtPayMethod method = switch (m) {
            // 微信 JSAPI / 小程序
            case WECHAT_JSAPI, WECHAT_MINI -> HkrtPayMethod.WECHAT_JSAPI;
            // 支付宝扫码(当面付二维码)
            case ALIPAY_QR -> HkrtPayMethod.ALIPAY_QR;
            // 支付宝 JSAPI / 小程序
            case ALIPAY_JSAPI, ALIPAY_MINI -> HkrtPayMethod.ALIPAY_JSAPI;
            // 银联二维码(云闪付)
            case UNION_QR -> HkrtPayMethod.UNION_QR;
            // 条码支付(微信/支付宝/银联付款码被扫)
            case WECHAT_BARCODE, ALIPAY_BARCODE, UNION_PAY_BARCODE -> HkrtPayMethod.BARCODE;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.hkrtUnsupportedPayMethod", methodCode);
        };
        // 支付内容类型由 method 推导
        HkrtPayBodyType bodyType = switch (method) {
            case WECHAT_JSAPI, ALIPAY_JSAPI -> HkrtPayBodyType.JSAPI;
            case ALIPAY_QR, UNION_QR -> HkrtPayBodyType.QR_CODE;
            case BARCODE -> null;
        };
        return new MethodMapping(method, bodyType);
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(HkrtPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 HkrtPayBodyType → 平台 PayBodyTypeEnum)
        // 海科融通无 LINK 类型
        if (resp.getPayBodyType() != null) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 完成时间与金额(条码同步成功时返回)
        bo.setFinishTime(resp.getFinishTime());
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getPayerAmount());
        bo.setBuyerPayAmount(resp.getPayerAmount());
        // 用户标识
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(HkrtPayMethod method, HkrtPayBodyType bodyType) {
    }
}
