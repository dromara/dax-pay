package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouInternetChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouInternetPayMethod;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayBodyType;
import cn.daxpay.open.channel.fuyou.client.req.FuyouInternetPayReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouPayResp;
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
import java.util.Objects;

/// # 富友互联网直连支付执行业务服务
///
/// 通过 [FuyouInternetChannelClient] 调用子应用 dax-pay-channel-two 完成富友互联网直连支付下单
/// (hlwnets/aggpcpay 网关, JSON + RSA 加密信封)。
///
/// 富友互联网通道: 微信/支付宝/银联的扫码(order.fuiou) + 微信 JSAPI/小程序 + 支付宝 JS(FWC)。
/// 商户订单号直接使用平台交易号(order_id), 回调凭 order_id 原样反查, 无需前缀中转。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouInternetPayService {

    private final FuyouInternetChannelClient fuyouInternetChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行富友互联网直连支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, FuyouRsaSdkCredential credential) {
        MethodMapping mapping = mapMethod(payParam.getMethod());

        FuyouInternetPayReq req = new FuyouInternetPayReq();
        // 平台交易号直接作为富友 order_id(回调原样返回凭此反查)
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method);
        // 通道专属参数透传
        req.setOpenId(payParam.getOpenId());
        // 微信 JSAPI/小程序 appid 透传
        req.setWxAppId(payParam.getChannelAppId());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        DaxResult<FuyouPayResp> result = fuyouInternetChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.fuyou.payFailed", result.getMsg());
        }
        return toPayResult(result.getData());
    }

    /// 生成富友互联网支付异步通知地址(富友→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/fuyou/internet/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/fuyou/internet/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 富友互联网支付方式 + 内容类型
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 微信
            case WECHAT_JSAPI -> new MethodMapping(FuyouInternetPayMethod.WECHAT_JSAPI, FuyouPayBodyType.JSAPI);
            case WECHAT_MINI -> new MethodMapping(FuyouInternetPayMethod.WECHAT_MINI, FuyouPayBodyType.JSAPI);
            case WECHAT_QR -> new MethodMapping(FuyouInternetPayMethod.WECHAT_QR, FuyouPayBodyType.QR_CODE);
            // 支付宝
            case ALIPAY_JSAPI -> new MethodMapping(FuyouInternetPayMethod.ALIPAY_JSAPI, FuyouPayBodyType.IDENTIFIER);
            case ALIPAY_QR -> new MethodMapping(FuyouInternetPayMethod.ALIPAY_QR, FuyouPayBodyType.QR_CODE);
            // 银联
            case UNION_QR -> new MethodMapping(FuyouInternetPayMethod.UNION_QR, FuyouPayBodyType.QR_CODE);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.fuyou.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(FuyouPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getOutOrderNo())
                // 富联网商户订单号(order_id = 平台交易号), 用于回调/同步反查
                .setRelationOrderNo(resp.getRelationOrderNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody())
                .setTradeProduct(resp.getTradeProduct());

        // 支付内容类型映射(子应用 FuyouPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }

        // 完成时间与金额(同步终态时返回)
        bo.setFinishTime(resp.getFinishTime());
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerPayAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(FuyouInternetPayMethod method, FuyouPayBodyType bodyType) {
    }
}
