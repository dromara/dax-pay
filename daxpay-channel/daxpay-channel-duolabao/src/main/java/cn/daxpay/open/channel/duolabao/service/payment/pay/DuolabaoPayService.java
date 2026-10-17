package cn.daxpay.open.channel.duolabao.service.payment.pay;

import cn.daxpay.open.channel.duolabao.client.DuolabaoChannelClient;
import cn.daxpay.open.channel.duolabao.client.credential.DuolabaoSdkCredential;
import cn.daxpay.open.channel.duolabao.client.enums.DuolabaoPayBodyType;
import cn.daxpay.open.channel.duolabao.client.enums.DuolabaoPayMethod;
import cn.daxpay.open.channel.duolabao.client.req.DuolabaoPayReq;
import cn.daxpay.open.channel.duolabao.client.resp.DuolabaoPayResp;
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

/// # 哆啦宝支付执行业务服务
///
/// 通过 [DuolabaoChannelClient] 调用子应用 dax-pay-channel-two 完成哆啦宝下单。
/// 哆啦宝为聚合通道: 一张聚合码承载微信/支付宝/云闪付主扫(通道侧自动识别底层渠道),
/// 另支持云闪付 H5 跳转与支付宝/微信 JS 主动支付。
///
/// openId 校验在子应用 fail-fast; 本类仅做平台支付方式白名单映射。
@Slf4j
@Service
@RequiredArgsConstructor
public class DuolabaoPayService {

    private final DuolabaoChannelClient duolabaoChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行哆啦宝支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, DuolabaoSdkCredential credential) {
        // 平台支付方式 → 哆啦宝支付方式(白名单校验)
        DuolabaoPayMethod payMethod = this.mapMethod(payParam.getMethod());

        // 构建请求
        DuolabaoPayReq req = new DuolabaoPayReq();
        // 使用支付交易号作为商户订单号透传给哆啦宝(requestNum), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setPayMethod(payMethod);
        // JS 支付场景的用户标识(支付宝买家用户ID / 微信 openid)
        req.setOpenId(payParam.getOpenId());
        // 通道应用 AppId(微信 JS 上送 appId/subAppId)
        req.setChannelAppId(payParam.getChannelAppId());
        // 商品标题
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        // 同步跳转地址(聚合码支付完成后跳转)
        req.setReturnUrl(payParam.getReturnUrl());
        // 订单过期时间(空则子应用按默认有效期兜底)
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<DuolabaoPayResp> result = duolabaoChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 哆啦宝支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.duolabao.payFailed", result.getMsg());
        }

        return this.toPayResult(result.getData());
    }

    /// 生成哆啦宝支付异步通知地址(哆啦宝→平台, callbackUrl)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/duolabao/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/duolabao/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 哆啦宝支付方式(白名单映射)
    ///
    /// 扫码族(微信/支付宝/云闪付)与云闪付 H5 共用同一张聚合码接口;
    /// JS 族走 createPayWithCheck(支付宝=买家用户ID, 微信=openid+appId)。
    private static DuolabaoPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 聚合扫码: 微信/支付宝/云闪付扫同一张码
            case ALIPAY_QR, WECHAT_QR, UNION_QR -> DuolabaoPayMethod.valueOf(m.name());
            // 云闪付 H5(聚合码链接跳转)
            case UNION_H5 -> DuolabaoPayMethod.UNION_H5;
            // JS 主动支付
            case ALIPAY_JSAPI, WECHAT_JSAPI -> DuolabaoPayMethod.valueOf(m.name());
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.duolabao.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(DuolabaoPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应不携带哆啦宝订单号(orderNum 经回调/查单补充)
                // 聚合扫码/JS 支付均为异步受理, 下单时不完成
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 DuolabaoPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
            }
        }
        return bo;
    }
}
