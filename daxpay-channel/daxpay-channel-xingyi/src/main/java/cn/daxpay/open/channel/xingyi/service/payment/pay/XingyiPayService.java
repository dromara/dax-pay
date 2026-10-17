package cn.daxpay.open.channel.xingyi.service.payment.pay;

import cn.daxpay.open.channel.xingyi.client.XingyiChannelClient;
import cn.daxpay.open.channel.xingyi.client.credential.XingyiSdkCredential;
import cn.daxpay.open.channel.xingyi.client.enums.XingyiPayBodyType;
import cn.daxpay.open.channel.xingyi.client.enums.XingyiPayMethod;
import cn.daxpay.open.channel.xingyi.client.req.XingyiPayReq;
import cn.daxpay.open.channel.xingyi.client.resp.XingyiPayResp;
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

/// # 星驿付支付执行业务服务
///
/// 通过 [XingyiChannelClient] 调用子应用 dax-pay-channel-two 完成星驿付驿云付下单。
/// 星驿付为聚合通道: 官方码(支付宝/云闪付)、星驿码聚合(微信)、JSAPI(微信/支付宝)、
/// 微信小程序、付款码(微信/支付宝/云闪付)。
///
/// openId/authCode 校验在子应用 fail-fast; 本类仅做平台支付方式白名单映射。
@Slf4j
@Service
@RequiredArgsConstructor
public class XingyiPayService {

    private final XingyiChannelClient xingyiChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行星驿付支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, XingyiSdkCredential credential) {
        // 平台支付方式 → 星驿付支付方式(白名单校验)
        XingyiPayMethod payMethod = this.mapMethod(payParam.getMethod());

        // 构建请求
        XingyiPayReq req = new XingyiPayReq();
        // 使用支付交易号作为商户订单号透传给星驿付(orderNo), 回调时凭 THREE_ORDER_NO 反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setPayMethod(payMethod);
        // JSAPI/MINI 场景的用户标识(微信 openid / 支付宝买家用户ID)
        req.setOpenId(payParam.getOpenId());
        // 通道应用 AppId(微信 JSAPI/小程序上送 wxAppid)
        req.setChannelAppId(payParam.getChannelAppId());
        // 付款码场景的授权码
        req.setAuthCode(payParam.getAuthCode());
        // 商品标题(作为星驿付 title 字段): 优先描述, 为空回退标题
        req.setTitle(StrUtil.blankToDefault(payParam.getDescription(), payParam.getTitle()));
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<XingyiPayResp> result = xingyiChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 星驿付支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.xingyi.payFailed", result.getMsg());
        }

        return this.toPayResult(result.getData());
    }

    /// 生成星驿付支付异步通知地址(星驿付→平台, asyncNotify)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/xingyi_pay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/xingyi_pay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 星驿付支付方式(白名单映射)
    private static XingyiPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 扫码: 支付宝官方码 / 云闪付官方码 / 微信星驿码聚合
            case ALIPAY_QR, UNION_QR, WECHAT_QR -> XingyiPayMethod.valueOf(m.name());
            // JSAPI / 小程序
            case ALIPAY_JSAPI, WECHAT_JSAPI, WECHAT_MINI -> XingyiPayMethod.valueOf(m.name());
            // 付款码: 平台已识别分钱包 method, 通道折叠为统一被扫 API
            case WECHAT_BARCODE, ALIPAY_BARCODE, UNION_BARCODE -> XingyiPayMethod.valueOf(m.name());
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.xingyi.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(XingyiPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应携带星驿付订单号则回写, 供后续查单/退款; 可能为空(经回调补充)
                .setOutOrderNo(resp.getOutOrderNo())
                // 付款码同步交易可能即时完成, 扫码/JSAPI 族恒 false
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 XingyiPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
            }
        }
        return bo;
    }
}
