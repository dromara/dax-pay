package cn.daxpay.open.channel.fubei.service.payment.pay;

import cn.daxpay.open.channel.fubei.client.FubeiChannelClient;
import cn.daxpay.open.channel.fubei.client.credential.FubeiSdkCredential;
import cn.daxpay.open.channel.fubei.client.enums.FubeiPayMethod;
import cn.daxpay.open.channel.fubei.client.req.FubeiPayReq;
import cn.daxpay.open.channel.fubei.client.resp.FubeiPayResp;
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

/// # 付呗支付执行业务服务
///
/// 通过 [FubeiChannelClient] 调用子应用 dax-pay-channel-two 完成付呗开放平台统一下单。
/// 付呗聚合通道: 云闪付扫码 / 微信JSAPI / 支付宝JSAPI / 支付宝H5。
///
/// openId/sub_appid 校验在子应用 fail-fast; 本类仅做平台支付方式白名单映射。
@Slf4j
@Service
@RequiredArgsConstructor
public class FubeiPayService {

    private final FubeiChannelClient fubeiChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行付呗支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, FubeiSdkCredential credential) {
        // 平台支付方式 → 付呗支付方式(白名单校验)
        FubeiPayMethod payMethod = this.mapMethod(payParam.getMethod());

        // 构建请求
        FubeiPayReq req = new FubeiPayReq();
        // 使用支付交易号作为商户订单号透传给付呗(merchant_order_sn), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setPayMethod(payMethod);
        // JSAPI 族用户标识(微信 openid / 支付宝买家用户ID)
        req.setOpenId(payParam.getOpenId());
        // 微信 JSAPI 上送 sub_appid(公众号/小程序 appid)
        req.setChannelAppId(payParam.getChannelAppId());
        // 商品标题(作为付呗 body 字段)
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<FubeiPayResp> result = fubeiChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.fubei.payFailed", result.getMsg());
        }

        return this.toPayResult(result.getData());
    }

    /// 生成付呗支付异步通知地址(付呗→平台, notify_url)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/fubei/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/fubei/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 付呗支付方式(白名单映射)
    private static FubeiPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 云闪付扫码(→ qr_code)
            case UNION_QR -> FubeiPayMethod.UNION_QR;
            // JSAPI 族(微信=openid+sub_appid / 支付宝=买家用户ID)
            case WECHAT_JSAPI, WECHAT_MINI -> FubeiPayMethod.WECHAT_JSAPI;
            case ALIPAY_JSAPI -> FubeiPayMethod.ALIPAY_JSAPI;
            // 支付宝 H5(wap.create → 表单 HTML/跳转 URL)
            case ALIPAY_H5 -> FubeiPayMethod.ALIPAY_H5;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.fubei.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(FubeiPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应携带付呗订单号则回写, 供后续查单/退款; 可能为空(经回调补充)
                .setOutOrderNo(resp.getOutOrderNo())
                // 扫码/JSAPI/H5 族为异步支付, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 FubeiPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case FROM -> bo.setPayBodyType(PayBodyTypeEnum.FROM);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
