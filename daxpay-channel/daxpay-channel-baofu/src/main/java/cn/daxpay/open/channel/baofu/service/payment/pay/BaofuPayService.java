package cn.daxpay.open.channel.baofu.service.payment.pay;

import cn.daxpay.open.channel.baofu.client.BaofuChannelClient;
import cn.daxpay.open.channel.baofu.client.credential.BaofuSdkCredential;
import cn.daxpay.open.channel.baofu.client.enums.BaofuPayBodyType;
import cn.daxpay.open.channel.baofu.client.enums.BaofuPayMethod;
import cn.daxpay.open.channel.baofu.client.req.BaofuPayReq;
import cn.daxpay.open.channel.baofu.client.resp.BaofuPayResp;
import cn.daxpay.open.channel.baofu.strategy.product.BaofuDirectProductStrategy;
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

/// # 宝付支付执行业务服务
///
/// 通过 [BaofuChannelClient] 调用子应用 dax-pay-channel-two 完成宝付聚合网关下单。
/// 宝付聚合通道: 聚合收银台(pre_unified_order)/标准下单(unified_order)双形态,
/// 按平台支付方式映射 payCode 与支付内容类型。
///
/// 微信 JSAPI/小程序需 sub_appid(payExtend.sub_appid),
/// 通过微信开放应用门面解析商户/平台级微信应用(通道能力绑 → 产品级默认绑 → 平台应用推导)。
@Slf4j
@Service
@RequiredArgsConstructor
public class BaofuPayService {

    private final BaofuChannelClient baofuChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final WxAppFacade wxAppFacade;
    private final BaofuDirectProductStrategy productStrategy;

    /// 执行宝付支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, BaofuSdkCredential credential) {
        // 平台支付方式 → 宝付支付方式
        BaofuPayMethod method = mapMethod(payParam.getMethod());

        // 构建请求
        BaofuPayReq req = new BaofuPayReq();
        // 使用支付交易号作为商户订单号透传给宝付(outTradeNo), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setMethod(method);
        // 通道专属参数透传(微信 sub_openid / 支付宝 buyer_id / 云闪付 userId; 付款码 authCode)
        req.setOpenId(payParam.getOpenId());
        req.setAuthCode(payParam.getAuthCode());
        req.setClientIp(payParam.getClientIp());
        req.setReturnUrl(payParam.getReturnUrl());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);
        // 微信 JSAPI/小程序需 sub_appid;
        // 通过微信开放应用门面解析商户/平台级微信应用(通道能力绑 → 产品级默认绑 → 平台应用推导)
        if (isWechatJsapiOrMini(payParam.getMethod())) {
            WxAppView wxApp = wxAppFacade.resolve(
                    order.getMchNo(), payParam.getChannelMchNo(),
                    payParam.getCapability(), payParam.getChannelAppId(), ProductEnum.BAOFU_PAY.getCode());
            req.setWxAppId(wxApp.wxAppId());
        }

        // 调用子应用
        DaxResult<BaofuPayResp> result = baofuChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.baofu.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成宝付支付异步通知地址(宝付→平台, notifyUrl)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/baofu/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/baofu/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 是否微信 JSAPI/小程序支付(payExtend 需 sub_appid;
    /// 判定范围由产品策略 [BaofuDirectProductStrategy#wxAppRequiredCapabilities] 声明)
    private boolean isWechatJsapiOrMini(String methodCode) {
        PayMethodEnum method = PayMethodEnum.findByCode(methodCode);
        return ProductStrategySupport.capabilitiesForMethod(productStrategy, method).stream()
                .anyMatch(productStrategy.wxAppRequiredCapabilities()::contains);
    }

    /// 平台支付方式([PayMethodEnum] code)校验与映射
    ///
    /// 宝付支持: 聚合收银台(微信/支付宝/聚合扫码共用)/微信 JSAPI/小程序/付款码/
    /// 支付宝扫码/JS/付款码/云闪付扫码/云闪付 JS;
    /// H5/APP 等其余方式一期未接(协议 WECHAT_APP 参考 H5 收银台形态未覆盖, 见产品策略注释)。
    private static BaofuPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 聚合扫码/微信扫码: 聚合收银台承载(一码多渠道, 通道侧自动识别)
            case AGGREGATE_PAY_QRCODE, WECHAT_QR -> BaofuPayMethod.AGGREGATE_QR;
            case ALIPAY_QR -> BaofuPayMethod.ALIPAY_QR;
            case ALIPAY_JSAPI -> BaofuPayMethod.ALIPAY_JSAPI;
            case WECHAT_JSAPI -> BaofuPayMethod.WECHAT_JSAPI;
            case WECHAT_MINI -> BaofuPayMethod.WECHAT_JSAPI;
            case WECHAT_BARCODE -> BaofuPayMethod.WECHAT_BARCODE;
            case ALIPAY_BARCODE -> BaofuPayMethod.ALIPAY_BARCODE;
            case UNION_QR -> BaofuPayMethod.UNION_QR;
            case UNION_JSAPI -> BaofuPayMethod.UNION_JSAPI;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.baofu.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(BaofuPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 下单响应若携带宝付交易号则回写, 供后续查单/退款; 可能为空(经回调补充)
                .setOutOrderNo(resp.getOutOrderNo())
                // 下单恒为异步支付(含付款码被扫), 等待异步通知确认
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 BaofuPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
