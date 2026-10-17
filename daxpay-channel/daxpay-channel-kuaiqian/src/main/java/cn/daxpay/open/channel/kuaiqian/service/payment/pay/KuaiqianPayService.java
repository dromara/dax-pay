package cn.daxpay.open.channel.kuaiqian.service.payment.pay;

import cn.daxpay.open.channel.kuaiqian.client.KuaiqianChannelClient;
import cn.daxpay.open.channel.kuaiqian.client.credential.KuaiqianSdkCredential;
import cn.daxpay.open.channel.kuaiqian.client.enums.KuaiqianPayMethod;
import cn.daxpay.open.channel.kuaiqian.client.req.KuaiqianPayReq;
import cn.daxpay.open.channel.kuaiqian.client.resp.KuaiqianPayResp;
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

/// # 快钱支付执行业务服务
///
/// 通过 [KuaiqianChannelClient] 调用子应用 dax-pay-channel-two 完成快钱 A7007 当面付聚合扫码下单。
/// 快钱聚合通道: 一张聚合码承载微信/支付宝/云闪付主扫, 通道侧自动识别底层渠道。
@Slf4j
@Service
@RequiredArgsConstructor
public class KuaiqianPayService {

    private final KuaiqianChannelClient kuaiqianChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行快钱支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, KuaiqianSdkCredential credential) {
        // 构建请求
        KuaiqianPayReq req = new KuaiqianPayReq();
        // 使用支付交易号作为商户订单号透传给快钱(externalRefNumber), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);
        // 支付方式校验(一期仅聚合扫码, qrType=00)
        this.checkMethod(payParam.getMethod());

        // 调用子应用
        DaxResult<KuaiqianPayResp> result = kuaiqianChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.kuaiqian.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成快钱支付异步通知地址(快钱→平台, tr3Url)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/kuaiqian/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/kuaiqian/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code)校验
    ///
    /// 快钱当面付 qrType=00 为聚合主扫码: 聚合扫码/微信扫码/支付宝扫码/云闪付扫码均落同一张聚合码;
    /// 其余方式(条码/JSAPI/H5/小程序等)一期未接。
    private static KuaiqianPayMethod checkMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case AGGREGATE_PAY_QRCODE, WECHAT_QR, ALIPAY_QR, UNION_QR -> KuaiqianPayMethod.QR;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.kuaiqian.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(KuaiqianPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应若携带订单控制号则回写, 供后续查单/退款; 可能为空(经回调补充)
                .setOutOrderNo(resp.getOutOrderNo())
                // 聚合扫码为异步支付, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 KuaiqianPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
            }
        }
        return bo;
    }
}
