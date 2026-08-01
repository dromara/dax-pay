package cn.daxpay.open.channel.union.service.payment.pay;

import cn.daxpay.open.channel.union.client.UnionChannelClient;
import cn.daxpay.open.channel.union.client.credential.UnionSdkCredential;
import cn.daxpay.open.channel.union.client.enums.UnionPayBodyType;
import cn.daxpay.open.channel.union.client.enums.UnionPayMethod;
import cn.daxpay.open.channel.union.client.req.UnionPayReq;
import cn.daxpay.open.channel.union.client.resp.UnionPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
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

/// # 云闪付支付执行业务服务
///
/// 通过 [UnionChannelClient] 调用子应用 dax-pay-channel-one 完成云闪付支付下单。
/// 银联为单一渠道(UNION_PAY), 平台支付方式直接映射为 [UnionPayMethod]。
@Slf4j
@Service
@RequiredArgsConstructor
public class UnionPayService {

    private final UnionChannelClient unionChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行云闪付支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, UnionSdkCredential credential) {
        UnionPayReq req = new UnionPayReq();
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setDescription(payParam.getTitle());
        req.setMethod(this.mapMethod(payParam.getMethod()));
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);
        // 被扫支付需付款码
        if (req.getMethod() == UnionPayMethod.BARCODE) {
            req.setAuthCode(payParam.getAuthCode());
        }

        DaxResult<UnionPayResp> result = unionChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.union.payFailed", result.getMsg());
        }
        return toPayResult(result.getData());
    }

    /// 生成云闪付支付异步通知地址(银联→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/union/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/union/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) -> 云闪付通道支付方式
    private static UnionPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case UNION_QR -> UnionPayMethod.QRCODE;
            case UNION_H5 -> UnionPayMethod.H5;
            case UNION_BARCODE -> UnionPayMethod.BARCODE;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.union.notSupportMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(UnionPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo().setPayBody(resp.getPayBody());
        UnionPayBodyType bodyType = resp.getPayBodyType();
        if (bodyType != null) {
            switch (bodyType) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
