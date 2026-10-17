package cn.daxpay.open.channel.fuyou.service.payment;

import cn.daxpay.open.channel.fuyou.client.FuyouH5ChannelClient;
import cn.daxpay.open.channel.fuyou.client.credential.FuyouRsaSdkCredential;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouH5PayMethod;
import cn.daxpay.open.channel.fuyou.client.enums.FuyouPayBodyType;
import cn.daxpay.open.channel.fuyou.client.req.FuyouH5PayReq;
import cn.daxpay.open.channel.fuyou.client.resp.FuyouPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
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
import java.util.Objects;

/// # 富友 H5 聚合收银台支付执行业务服务
///
/// 通过 [FuyouH5ChannelClient] 调用子应用 dax-pay-channel-two 完成富友 H5 聚合收银台支付下单
/// (aggpcpay token 网关, JSON + RSA 加密信封), 响应 token_url(收银台链接/小程序 scheme)。
///
/// 富友收银台通道: 支付宝扫码(AliPayJL) / 微信小程序(WeAppletPay) / 云闪付(AppQuickPay)。
/// 商户订单号直接使用平台交易号(order_id), 回调凭 order_id 原样反查, 无需前缀中转。
@Slf4j
@Service
@RequiredArgsConstructor
public class FuyouH5PayService {

    private final FuyouH5ChannelClient fuyouH5ChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行富友 H5 收银台支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, FuyouRsaSdkCredential credential) {
        MethodMapping mapping = mapMethod(payParam.getMethod());

        FuyouH5PayReq req = new FuyouH5PayReq();
        // 平台交易号直接作为富友 order_id(回调原样返回凭此反查)
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method);
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        // 同步跳转地址(支付完成浏览器跳转收银台回跳)
        req.setReturnUrl(payParam.getReturnUrl());
        req.setCredential(credential);

        DaxResult<FuyouPayResp> result = fuyouH5ChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.fuyou.payFailed", result.getMsg());
        }
        return toPayResult(result.getData());
    }

    /// 生成富友 H5 收银台支付异步通知地址(富友→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/fuyou/h5/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/fuyou/h5/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 富友收银台支付方式 + 内容类型
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 支付宝扫码(经收银台, token_url 渲染为二维码)
            case ALIPAY_QR -> new MethodMapping(FuyouH5PayMethod.ALIPAY_QR, FuyouPayBodyType.QR_CODE);
            // 微信小程序(token_url 为 scheme 跳转链接)
            case WECHAT_MINI -> new MethodMapping(FuyouH5PayMethod.WECHAT_MINI, FuyouPayBodyType.LINK);
            // 银联云闪付 App(经收银台, token_url 渲染为二维码)
            case UNION_QR -> new MethodMapping(FuyouH5PayMethod.UNION_QR, FuyouPayBodyType.QR_CODE);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.fuyou.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(FuyouPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getOutOrderNo())
                // 商户订单号(order_id = 平台交易号), 用于回调/同步反查
                .setRelationOrderNo(resp.getRelationOrderNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody())
                .setTradeProduct(resp.getTradeProduct());

        // 支付内容类型映射(子应用 FuyouPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                default -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
            }
        }

        // 完成时间与金额(同步终态时返回)
        bo.setFinishTime(resp.getFinishTime());
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerPayAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(FuyouH5PayMethod method, FuyouPayBodyType bodyType) {
    }
}
