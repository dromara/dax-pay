package cn.daxpay.open.channel.yizhifubj.service.payment.pay;

import cn.daxpay.open.channel.yizhifubj.client.YizhifubjChannelClient;
import cn.daxpay.open.channel.yizhifubj.client.credential.YizhifubjSdkCredential;
import cn.daxpay.open.channel.yizhifubj.client.enums.YizhifubjPayBodyType;
import cn.daxpay.open.channel.yizhifubj.client.enums.YizhifubjPayMethod;
import cn.daxpay.open.channel.yizhifubj.client.req.YizhifubjPayReq;
import cn.daxpay.open.channel.yizhifubj.client.resp.YizhifubjPayResp;
import cn.daxpay.open.channel.yizhifubj.strategy.product.YizhifubjDirectProductStrategy;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.strategy.ProductStrategySupport;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.payment.wx.facade.WxAppFacade;
import cn.daxpay.open.payment.wx.facade.WxAppView;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.pay.channel.ProductEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 一直付支付执行业务服务
///
/// 通过 [YizhifubjChannelClient] 调用子应用 dax-pay-channel-two 完成 5upay `/onlinePay/order` 统一下单。
/// 一直付为聚合通道: 按 paymentModeCode 区分底层渠道(微信/支付宝/云闪付)与支付形态(扫码/JsApi/WAP)。
///
/// 金额: 平台内部单位「分」(Long), 5upay 接口同为「分」, 直接透传。
@Slf4j
@Service
@RequiredArgsConstructor
public class YizhifubjPayService {

    private final YizhifubjChannelClient yizhifubjChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final WxAppFacade wxAppFacade;
    private final YizhifubjDirectProductStrategy productStrategy;

    /// 执行一直付支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, YizhifubjSdkCredential credential) {
        // 平台支付方式 → 一直付支付方式(paymentModeCode)
        YizhifubjPayMethod method = mapMethod(payParam.getMethod());

        // 构建请求
        YizhifubjPayReq req = new YizhifubjPayReq();
        // 使用支付交易号作为商户订单号透传给一直付(requestId), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        // 同步跳转地址(returnUrl → callbackUrl, 可空)
        req.setReturnUrl(payParam.getReturnUrl());
        req.setMethod(method);
        // JsApi 用户标识透传(支付宝 = 买家 userId / 微信 = openid)
        req.setOpenId(payParam.getOpenId());
        req.setCredential(credential);
        // 微信公众号 JsApi 需 appId 上送拉起收银台;
        // 通过微信开放应用门面解析商户/平台级微信应用(通道能力绑 → 产品级默认绑 → 平台应用推导)
        if (isWechatJsapi(payParam.getMethod())) {
            WxAppView wxApp = wxAppFacade.resolve(
                    order.getMchNo(), payParam.getChannelMchNo(),
                    payParam.getCapability(), payParam.getChannelAppId(), ProductEnum.YIZHIFU_PAY.getCode());
            req.setAppId(wxApp.wxAppId());
        }

        // 调用子应用
        DaxResult<YizhifubjPayResp> result = yizhifubjChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.yizhifubj.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成一直付支付异步通知地址(一直付→平台, notifyUrl)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/yizhifubj/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/yizhifubj/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 是否微信公众号 JsApi 支付(需 appId 上送; 判定范围由产品策略 [YizhifubjDirectProductStrategy#wxAppRequiredCapabilities] 声明)
    private boolean isWechatJsapi(String methodCode) {
        PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
        return ProductStrategySupport.capabilitiesForMethod(productStrategy, method).stream()
                .anyMatch(productStrategy.wxAppRequiredCapabilities()::contains);
    }

    /// 平台支付方式([PayMethodEnum] code) → 一直付支付方式
    private static YizhifubjPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 扫码(支付宝/微信/云闪付主扫, 通道按 paymentModeCode 分渠道出码)
            case ALIPAY_QR -> YizhifubjPayMethod.ALIPAY_QR;
            case WECHAT_QR -> YizhifubjPayMethod.WECHAT_QR;
            case UNION_QR -> YizhifubjPayMethod.UNION_QR;
            // 支付宝页面跳转(WAP-P2P 同步跳转, PC 浏览器共用此形态)
            case ALIPAY_PC -> YizhifubjPayMethod.ALIPAY_WAP;
            // JsApi(支付宝需 openId=买家 userId, 微信需 openId+appId)
            case ALIPAY_JSAPI -> YizhifubjPayMethod.ALIPAY_JSAPI;
            case WECHAT_JSAPI -> YizhifubjPayMethod.WECHAT_JSAPI;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.yizhifubj.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(YizhifubjPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应若携带通道流水号则回写, 供退款; 可能为空(经回调补充)
                .setOutOrderNo(resp.getOutOrderNo())
                // 扫码/JsApi/WAP 均为异步支付, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 YizhifubjPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case REDIRECT -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
            }
        }
        return bo;
    }
}
