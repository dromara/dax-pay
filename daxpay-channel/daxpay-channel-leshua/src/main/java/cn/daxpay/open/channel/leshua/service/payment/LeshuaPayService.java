package cn.daxpay.open.channel.leshua.service.payment;

import cn.daxpay.open.channel.leshua.client.LeshuaChannelClient;
import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.enums.LeshuaPayBodyType;
import cn.daxpay.open.channel.leshua.client.enums.LeshuaPayMethod;
import cn.daxpay.open.channel.leshua.client.req.LeshuaPayReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaPayResp;
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
import org.springframework.stereotype.Service;

/// # 乐刷服务商支付执行业务服务
///
/// 通过 [LeshuaChannelClient] 调用子应用 dax-pay-channel-two 完成乐刷支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 乐刷聚合通道: 通过 `pay_way`(底层渠道: 微信/支付宝/云闪付) + `jspay_flag`(支付形态: 扫码/JSAPI/H5/小程序) 组合决定路由。
@lombok.extern.slf4j.Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaPayService {

    private final LeshuaChannelClient leshuaChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行乐刷支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, LeshuaSdkCredential credential) {
        // 平台支付方式 → 乐刷三要素(method + payWay + jspayFlag + bodyType)
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        LeshuaPayReq req = new LeshuaPayReq();
        // 使用支付交易号作为商户订单号透传给乐刷, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method);
        req.setPayWay(mapping.payWay);
        req.setJspayFlag(mapping.jspayFlag);
        req.setPayBodyType(mapping.bodyType);
        // 通道专属参数透传
        req.setOpenId(payParam.getOpenId());
        req.setAuthCode(payParam.getAuthCode());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<LeshuaPayResp> result = leshuaChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.leshua.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成乐刷支付异步通知地址(乐刷→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/leshua/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/leshua/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 乐刷三要素
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 条码支付(微信/支付宝/银联付款码, 走 upload_authcode, 乐刷据 authCode 自动识别底层渠道)
            case WECHAT_BARCODE, ALIPAY_BARCODE, UNION_BARCODE ->
                    new MethodMapping(LeshuaPayMethod.UPLOAD_AUTHCODE, null, null, null);
            // 微信预下单(JSAPI 公众号 / MINI 小程序)
            case WECHAT_JSAPI -> new MethodMapping(LeshuaPayMethod.GET_TDCODE, "WXZF", "1", LeshuaPayBodyType.JSAPI);
            case WECHAT_MINI -> new MethodMapping(LeshuaPayMethod.GET_TDCODE, "WXZF", "3", LeshuaPayBodyType.JSAPI);
            // 支付宝预下单(扫码 / JSAPI / 小程序)
            case ALIPAY_QR -> new MethodMapping(LeshuaPayMethod.GET_TDCODE, "ZFBZF", "0", LeshuaPayBodyType.QR_CODE);
            case ALIPAY_JSAPI -> new MethodMapping(LeshuaPayMethod.GET_TDCODE, "ZFBZF", "1", LeshuaPayBodyType.IDENTIFIER);
            // 云闪付预下单(扫码 / JSAPI)
            case UNION_QR -> new MethodMapping(LeshuaPayMethod.GET_TDCODE, "UPSMZF", "0", LeshuaPayBodyType.QR_CODE);
            case UNION_JSAPI -> new MethodMapping(LeshuaPayMethod.GET_TDCODE, "UPSMZF", "1", LeshuaPayBodyType.LINK);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.leshua.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(LeshuaPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getLeshuaOrderId())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 LeshuaPayBodyType → 平台 PayBodyTypeEnum)
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
    private record MethodMapping(LeshuaPayMethod method, String payWay, String jspayFlag,
                                  LeshuaPayBodyType bodyType) {
    }
}
