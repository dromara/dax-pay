package cn.daxpay.open.channel.jlpay.service.payment.pay;

import cn.daxpay.open.channel.jlpay.client.JlpayChannelClient;
import cn.daxpay.open.channel.jlpay.client.credential.JlpaySdkCredential;
import cn.daxpay.open.channel.jlpay.client.enums.JlpayPayMethod;
import cn.daxpay.open.channel.jlpay.client.req.JlpayPayReq;
import cn.daxpay.open.channel.jlpay.client.resp.JlpayPayResp;
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

/// # 嘉联支付执行业务服务
///
/// 通过 [JlpayChannelClient] 调用子应用 dax-pay-channel-two 完成嘉联 OpenAPI 下单。
/// 嘉联聚合通道: 一期支持微信/支付宝/云闪付的扫码主扫(qrcodepay)与付款码被扫(micropay 自动识别渠道)。
///
/// 金额: 平台内部单位为「分」(Long), 嘉联接口 total_fee 同为「分」, 直接透传。
@Slf4j
@Service
@RequiredArgsConstructor
public class JlpayPayService {

    private final JlpayChannelClient jlpayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行嘉联支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, JlpaySdkCredential credential) {
        // 平台支付方式 → 嘉联支付方式(主扫/被扫 + 渠道类型)
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        JlpayPayReq req = new JlpayPayReq();
        // 使用支付交易号作为商户订单号透传给嘉联(out_trade_no), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method);
        req.setPayType(mapping.payType);
        req.setAuthCode(payParam.getAuthCode());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<JlpayPayResp> result = jlpayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.jlpay.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成嘉联支付异步通知地址(嘉联→平台, notify_url)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/jlpay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/jlpay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 嘉联支付方式 + 渠道类型
    ///
    /// 嘉联 qrcodepay 按渠道生成对应二维码(pay_type 上送), micropay 凭 auth_code 前缀自动识别渠道;
    /// JSAPI/小程序/APP/收银托管等形态一期未接(需 openId/微信应用绑定体系)。
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 扫码主扫(按渠道出码)
            case WECHAT_QR -> new MethodMapping(JlpayPayMethod.QRCODE, "wxpay");
            case ALIPAY_QR -> new MethodMapping(JlpayPayMethod.QRCODE, "alipay");
            case UNION_QR -> new MethodMapping(JlpayPayMethod.QRCODE, "unionpay");
            // 付款码被扫(嘉联据 auth_code 自动识别渠道, 不传 pay_type)
            case WECHAT_BARCODE, ALIPAY_BARCODE, UNION_BARCODE ->
                    new MethodMapping(JlpayPayMethod.MICROPAY, null);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.jlpay.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(JlpayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 嘉联交易号(transaction_id), 供后续查单/退款/撤销
                .setOutOrderNo(resp.getOutOrderNo())
                // 主扫异步支付 false; 被扫按同步扣款结果
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody())
                .setFinishTime(resp.getFinishTime())
                .setRealAmount(resp.getRealAmount())
                .setBuyerId(resp.getBuyerId());
        // 支付内容类型映射(子应用 JlpayPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
            }
        }
        return bo;
    }

    /// 支付方式映射内部封装(嘉联支付方式 + 渠道类型)
    private record MethodMapping(JlpayPayMethod method, String payType) {
    }
}
