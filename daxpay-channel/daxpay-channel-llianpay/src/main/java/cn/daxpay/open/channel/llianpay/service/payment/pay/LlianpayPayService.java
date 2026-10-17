package cn.daxpay.open.channel.llianpay.service.payment.pay;

import cn.daxpay.open.channel.llianpay.client.LlianpayChannelClient;
import cn.daxpay.open.channel.llianpay.client.credential.LlianpaySdkCredential;
import cn.daxpay.open.channel.llianpay.client.enums.LlianpayPayMethod;
import cn.daxpay.open.channel.llianpay.client.req.LlianpayPayReq;
import cn.daxpay.open.channel.llianpay.client.resp.LlianpayPayResp;
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

/// # 连连支付执行业务服务
///
/// 通过 [LlianpayChannelClient] 调用子应用 dax-pay-channel-two 完成连连统收单/收银台下单。
/// 连连为聚合通道: 扫码(微信/支付宝/云闪付)、JSAPI(微信/支付宝)、微信小程序、收银台跳转(云闪付/快捷)。
///
/// openId 校验在子应用 fail-fast; 本类仅做平台支付方式白名单映射。
@Slf4j
@Service
@RequiredArgsConstructor
public class LlianpayPayService {

    private final LlianpayChannelClient llianpayChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行连连支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, LlianpaySdkCredential credential) {
        // 平台支付方式 → 连连支付方式(白名单校验)
        LlianpayPayMethod payMethod = this.mapMethod(payParam.getMethod());

        // 构建请求
        LlianpayPayReq req = new LlianpayPayReq();
        // 使用支付交易号作为商户订单号透传给连连(txn_seqno), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setPayMethod(payMethod);
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        // JSAPI/MINI 场景的用户标识(微信 openid / 支付宝买家用户ID)
        req.setOpenId(payParam.getOpenId());
        // 通道应用 AppId(微信 JSAPI/小程序上送 wx_data.appid)
        req.setChannelAppId(payParam.getChannelAppId());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<LlianpayPayResp> result = llianpayChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 连连支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.llianpay.payFailed", result.getMsg());
        }

        return this.toPayResult(result.getData());
    }

    /// 生成连连支付异步通知地址(连连→平台, notify_url)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/llianpay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/llianpay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 连连支付方式(白名单映射)
    private static LlianpayPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 扫码族: 微信 / 支付宝 / 云闪付
            case WECHAT_QR -> LlianpayPayMethod.WECHAT_NATIVE;
            case ALIPAY_QR -> LlianpayPayMethod.ALIPAY_NATIVE;
            case UNION_QR -> LlianpayPayMethod.CLOUDPAY_APP;
            // JSAPI 族: 微信公众号 / 微信小程序 / 支付宝 JS
            case WECHAT_JSAPI -> LlianpayPayMethod.WECHAT_JSAPI;
            case WECHAT_MINI -> LlianpayPayMethod.WECHAT_APPLET;
            case ALIPAY_JSAPI -> LlianpayPayMethod.ALIPAY_APPLET;
            // 银联收银台跳转(承载云闪付/快捷等银行渠道)
            case UNION_JSAPI, UNION_H5 -> LlianpayPayMethod.CASHIER;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.llianpay.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(LlianpayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应若携带连连平台流水号则回写, 供后续查单/退款; 可能为空(经查单/回调补充)
                .setOutOrderNo(resp.getOutOrderNo())
                // 连连下单恒异步受理
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 LlianpayPayBodyType → 平台 PayBodyTypeEnum)
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
