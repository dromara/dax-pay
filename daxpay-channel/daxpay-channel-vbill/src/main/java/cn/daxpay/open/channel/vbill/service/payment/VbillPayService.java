package cn.daxpay.open.channel.vbill.service.payment;

import cn.daxpay.open.channel.vbill.client.VbillChannelClient;
import cn.daxpay.open.channel.vbill.client.credential.VbillSdkCredential;
import cn.daxpay.open.channel.vbill.client.enums.VbillPayBodyType;
import cn.daxpay.open.channel.vbill.client.enums.VbillPayMethod;
import cn.daxpay.open.channel.vbill.client.req.VbillPayReq;
import cn.daxpay.open.channel.vbill.client.resp.VbillPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
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

/// # 随行付服务商支付执行业务服务
///
/// 通过 [VbillChannelClient] 调用子应用 dax-pay-channel-two 完成随行付支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 随行付聚合通道: 4 个接口承载微信/支付宝/银联的 JSAPI/扫码/付款码/小程序收银台,
/// 由 [VbillPayMethod] + payType + payWay 决定路由。
@Slf4j
@Service
@RequiredArgsConstructor
public class VbillPayService {

    private final VbillChannelClient vbillChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行随行付支付
    ///
    /// @param order      支付订单(tradeNo 作为 ordNo)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(服务商密钥 + 天阙商户号)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, VbillSdkCredential credential) {
        // 平台支付方式 → 随行付三要素(method + payType + payWay + bodyType)
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        VbillPayReq req = new VbillPayReq();
        // 使用支付交易号作为商户订单号透传给随行付, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method);
        req.setPayType(mapping.payType);
        req.setPayWay(mapping.payWay);
        req.setPayBodyType(mapping.bodyType);
        // 通道专属参数透传
        req.setOpenId(payParam.getOpenId());
        req.setAuthCode(payParam.getAuthCode());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order));
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<VbillPayResp> result = vbillChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.vbillPayFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成随行付支付异步通知地址(随行付→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/vbill/pay`
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/vbill/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) → 随行付四要素
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 微信
            case WECHAT_JSAPI -> new MethodMapping(VbillPayMethod.UNI_PAY, "WECHAT", "02", VbillPayBodyType.JSAPI);
            case WECHAT_MINI -> new MethodMapping(VbillPayMethod.UNI_PAY, "WECHAT", "03", VbillPayBodyType.JSAPI);
            case WECHAT_QR -> new MethodMapping(VbillPayMethod.QR_CODE, null, null, VbillPayBodyType.QR_CODE);
            case WECHAT_BARCODE -> new MethodMapping(VbillPayMethod.BAR_CODE, null, null, null);
            case WECHAT_CASHIER -> new MethodMapping(VbillPayMethod.APPLET_CASHIER, null, null, VbillPayBodyType.JSAPI);
            // 支付宝
            case ALIPAY_JSAPI, ALIPAY_MINI -> new MethodMapping(VbillPayMethod.UNI_PAY, "ALIPAY", "02", VbillPayBodyType.IDENTIFIER);
            case ALIPAY_QR -> new MethodMapping(VbillPayMethod.QR_CODE, null, null, VbillPayBodyType.QR_CODE);
            case ALIPAY_BARCODE -> new MethodMapping(VbillPayMethod.BAR_CODE, null, null, null);
            // 银联
            case UNION_JSAPI -> new MethodMapping(VbillPayMethod.UNI_PAY, "UNIONPAY", "02", VbillPayBodyType.LINK);
            case UNION_QR -> new MethodMapping(VbillPayMethod.QR_CODE, null, null, VbillPayBodyType.QR_CODE);
            case UNION_PAY_BARCODE -> new MethodMapping(VbillPayMethod.BAR_CODE, null, null, null);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.vbillUnsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(VbillPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getOutOrderNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 VbillPayBodyType → 平台 PayBodyTypeEnum)
        if (resp.getPayBodyType() != null) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 完成时间与金额(付款码同步成功时返回)
        bo.setFinishTime(resp.getFinishTime());
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerPayAmount(resp.getRealAmount());
        // 用户标识
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(VbillPayMethod method, String payType, String payWay,
                                 VbillPayBodyType bodyType) {
    }
}
