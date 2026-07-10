package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouSdkCredential;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayBodyType;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayMethod;
import cn.daxpay.open.channel.fuyou.client.req.FuyouPayReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouPayResp;
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

/// # 富友服务商支付执行业务服务
///
/// 通过 [FuyouChannelClient] 调用子应用 dax-pay-channel-two 完成富友支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 富友聚合通道: 微信/支付宝/银联的扫码(/preCreate) + JSAPI(/wxPreCreate) + 付款码(/micropay)。
/// 关联订单号(relationOrderNo)由子应用生成(orderPrefix+雪花), 富友回调凭 mchnt_order_no 反查平台订单。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouPayService {

    private final FuyouChannelClient fuyouChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行富友支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, FuyouSdkCredential credential) {
        MethodMapping mapping = mapMethod(payParam.getMethod());

        FuyouPayReq req = new FuyouPayReq();
        // 平台交易号透传(子应用存入 addn_inf, 并作为 outTradeNo 返回)
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
        req.setCredential(credential);

        DaxResult<FuyouPayResp> result = fuyouChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.fuyouPayFailed", result.getMsg());
        }
        return toPayResult(result.getData());
    }

    /// 生成富友支付异步通知地址(富友→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/fuyou/pay`
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/fuyou/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) → 富友支付方式 + 内容类型
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 微信
            case WECHAT_JSAPI -> new MethodMapping(FuyouPayMethod.WECHAT_JSAPI, FuyouPayBodyType.JSAPI);
            case WECHAT_MINI -> new MethodMapping(FuyouPayMethod.WECHAT_MINI, FuyouPayBodyType.JSAPI);
            case WECHAT_QR -> new MethodMapping(FuyouPayMethod.WECHAT_QR, FuyouPayBodyType.QR_CODE);
            case WECHAT_BARCODE -> new MethodMapping(FuyouPayMethod.BARCODE, null);
            // 支付宝
            case ALIPAY_JSAPI, ALIPAY_MINI -> new MethodMapping(FuyouPayMethod.ALIPAY_JSAPI, FuyouPayBodyType.IDENTIFIER);
            case ALIPAY_QR -> new MethodMapping(FuyouPayMethod.ALIPAY_QR, FuyouPayBodyType.QR_CODE);
            case ALIPAY_BARCODE -> new MethodMapping(FuyouPayMethod.BARCODE, null);
            // 银联
            case UNION_QR -> new MethodMapping(FuyouPayMethod.UNION_QR, FuyouPayBodyType.QR_CODE);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.fuyouUnsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(FuyouPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getOutOrderNo())
                // 富友关联订单号(mchnt_order_no), 用于回调/同步反查
                .setRelationOrderNo(resp.getRelationOrderNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody())
                .setTradeProduct(resp.getTradeProduct());

        // 支付内容类型映射(子应用 FuyouPayBodyType → 平台 PayBodyTypeEnum)
        if (resp.getPayBodyType() != null) {
            switch (resp.getPayBodyType()) {
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
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(FuyouPayMethod method, FuyouPayBodyType bodyType) {
    }
}
