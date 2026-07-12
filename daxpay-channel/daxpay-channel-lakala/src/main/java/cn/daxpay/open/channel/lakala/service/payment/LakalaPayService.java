package cn.daxpay.open.channel.lakala.service.payment;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.LakalaSdkCredential;
import cn.daxpay.open.channel.lakala.client.enums.LakalaPayBodyType;
import cn.daxpay.open.channel.lakala.client.enums.LakalaPayMethod;
import cn.daxpay.open.channel.lakala.client.req.LakalaPayReq;
import cn.daxpay.open.channel.lakala.client.resp.LakalaPayResp;
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

/// # 拉卡拉服务商支付执行业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用 dax-pay-channel-two 完成拉卡拉支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 拉卡拉聚合通道: 一个接口承载微信/支付宝/银联三种底层渠道, 由 accountType + transType 决定路由。
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaPayService {

    private final LakalaChannelClient lakalaChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行拉卡拉支付
    ///
    /// @param order      支付订单(tradeNo 作为 out_trade_no)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(服务商密钥 + 商户号/终端号)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, LakalaSdkCredential credential) {
        // 平台支付方式 → 拉卡拉三要素(method + accountType + transType + bodyType)
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        LakalaPayReq req = new LakalaPayReq();
        // 使用支付交易号作为商户订单号透传给拉卡拉, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method);
        req.setAccountType(mapping.accountType);
        req.setTransType(mapping.transType);
        req.setPayBodyType(mapping.bodyType);
        // 通道专属参数透传
        req.setOpenId(payParam.getOpenId());
        req.setAuthCode(payParam.getAuthCode());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order));
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<LakalaPayResp> result = lakalaChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.lakalaPayFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成拉卡拉支付异步通知地址(拉卡拉→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/lakala/pay`
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/lakala/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) → 拉卡拉三要素
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 条码支付(微信/支付宝/银联付款码, 走 micropay, accountType 由拉卡拉自动识别)
            case WECHAT_BARCODE, ALIPAY_BARCODE, UNION_PAY_BARCODE ->
                    new MethodMapping(LakalaPayMethod.MICROPAY, null, null, null);
            // 微信预下单
            case WECHAT_JSAPI -> new MethodMapping(LakalaPayMethod.PREORDER, "WECHAT", "51", LakalaPayBodyType.JSAPI);
            case WECHAT_MINI -> new MethodMapping(LakalaPayMethod.PREORDER, "WECHAT", "71", LakalaPayBodyType.JSAPI);
            case WECHAT_APP -> new MethodMapping(LakalaPayMethod.PREORDER, "WECHAT", "61", LakalaPayBodyType.JSAPI);
            // 支付宝预下单
            case ALIPAY_QR -> new MethodMapping(LakalaPayMethod.PREORDER, "ALIPAY", "41", LakalaPayBodyType.QR_CODE);
            case ALIPAY_JSAPI, ALIPAY_MINI -> new MethodMapping(LakalaPayMethod.PREORDER, "ALIPAY", "51", LakalaPayBodyType.IDENTIFIER);
            // 银联预下单
            case UNION_QR -> new MethodMapping(LakalaPayMethod.PREORDER, "UQRCODEPAY", "41", LakalaPayBodyType.QR_CODE);
            case UNION_JSAPI -> new MethodMapping(LakalaPayMethod.PREORDER, "UQRCODEPAY", "51", LakalaPayBodyType.LINK);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.lakalaUnsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(LakalaPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 LakalaPayBodyType → 平台 PayBodyTypeEnum)
        if (resp.getPayBodyType() != null) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
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
    private record MethodMapping(LakalaPayMethod method, String accountType, String transType,
                                 LakalaPayBodyType bodyType) {
    }
}
