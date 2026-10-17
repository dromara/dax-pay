package cn.daxpay.open.channel.cpcn.service.payment.pay;

import cn.daxpay.open.channel.cpcn.client.CpcnChannelClient;
import cn.daxpay.open.channel.cpcn.client.credential.CpcnSdkCredential;
import cn.daxpay.open.channel.cpcn.client.enums.CpcnPayMethod;
import cn.daxpay.open.channel.cpcn.client.req.CpcnPayReq;
import cn.daxpay.open.channel.cpcn.client.resp.CpcnPayResp;
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

/// # 中金支付执行业务服务
///
/// 通过 [CpcnChannelClient] 调用子应用 dax-pay-channel-two 完成中金 5011 统一下单。
/// 按平台支付方式映射中金三级码(被扫 42 线: 微信/支付宝/云闪付扫码; 跳转 80 线: 微信 JSAPI/H5、支付宝 JSAPI)。
@Slf4j
@Service
@RequiredArgsConstructor
public class CpcnPayService {

    private final CpcnChannelClient cpcnChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行中金支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, CpcnSdkCredential credential) {
        // 构建请求
        CpcnPayReq req = new CpcnPayReq();
        // 使用支付交易号作为商户流水号 TxSN 上送, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);
        // JSAPI 族用户标识与公众号 AppId(微信=openid+channelAppId / 支付宝=买家用户ID)
        req.setOpenId(payParam.getOpenId());
        req.setAppId(payParam.getChannelAppId());
        // 支付方式校验与映射
        req.setPayMethod(this.checkMethod(payParam.getMethod()));

        // 调用子应用
        DaxResult<CpcnPayResp> result = cpcnChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.cpcn.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成中金支付异步通知地址(中金→平台, NoticeURL)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/cpcn/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/cpcn/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code)校验与中金三级码映射
    ///
    /// - 扫码族(42 线): 微信/支付宝/云闪付各自独立二维码(ScanPaymentType 区分)
    /// - 跳转族(80 线): 微信 JSAPI(需 openid+公众号 AppId) / 微信 H5 / 支付宝 JSAPI(需买家用户ID)
    /// - 其余方式(条码/小程序/聚合码等)一期未接
    private static CpcnPayMethod checkMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case WECHAT_QR -> CpcnPayMethod.WECHAT_QR;
            case ALIPAY_QR -> CpcnPayMethod.ALIPAY_QR;
            case UNION_QR -> CpcnPayMethod.UNION_QR;
            case WECHAT_JSAPI -> CpcnPayMethod.WECHAT_JSAPI;
            case WECHAT_H5 -> CpcnPayMethod.WECHAT_H5;
            case ALIPAY_JSAPI -> CpcnPayMethod.ALIPAY_JSAPI;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.cpcn.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(CpcnPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应若携带中金订单号则回写, 供对账; 可能为空(经回调补充)
                .setOutOrderNo(resp.getOutOrderNo())
                // 异步支付, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody())
                .setTotalAmount(resp.getTotalAmount());
        // 支付内容类型映射(子应用 CpcnPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
