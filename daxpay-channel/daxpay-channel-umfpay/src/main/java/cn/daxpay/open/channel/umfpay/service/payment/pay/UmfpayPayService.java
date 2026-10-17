package cn.daxpay.open.channel.umfpay.service.payment.pay;

import cn.daxpay.open.channel.umfpay.client.UmfpayChannelClient;
import cn.daxpay.open.channel.umfpay.client.credential.UmfpaySdkCredential;
import cn.daxpay.open.channel.umfpay.client.enums.UmfpayPayMethod;
import cn.daxpay.open.channel.umfpay.client.req.UmfpayPayReq;
import cn.daxpay.open.channel.umfpay.client.resp.UmfpayPayResp;
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

/// # 联动优势支付执行业务服务
///
/// 通过 [UmfpayChannelClient] 调用子应用 dax-pay-channel-two 完成联动优势老网关下单
/// (扫码族 active_scancode_order_new / 公众号 publicnumber_and_verticalcode)。
/// 联动优势为按渠道指定扫码的聚合通道: 微信/支付宝/云闪付扫码与微信公众号跳转。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmfpayPayService {

    private final UmfpayChannelClient umfpayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行联动优势支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, UmfpaySdkCredential credential) {
        // 平台支付方式 → 联动优势支付方式(白名单校验)
        UmfpayPayMethod payMethod = this.mapMethod(payParam.getMethod());

        // 构建请求
        UmfpayPayReq req = new UmfpayPayReq();
        // 使用支付交易号作为商户订单号透传给联动优势(order_id), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setPayMethod(payMethod);
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        // 公众号支付的支付完成同步跳转地址
        req.setReturnUrl(payParam.getReturnUrl());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<UmfpayPayResp> result = umfpayChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 联动优势支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.umfpay.payFailed", result.getMsg());
        }

        return this.toPayResult(result.getData());
    }

    /// 生成联动优势支付异步通知地址(联动优势→平台, notify_url)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/umfpay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/umfpay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 联动优势支付方式(白名单映射)
    private static UmfpayPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 扫码族: 微信 / 支付宝 / 云闪付(scancode_type 指定底层渠道)
            case WECHAT_QR -> UmfpayPayMethod.WECHAT_QR;
            case ALIPAY_QR -> UmfpayPayMethod.ALIPAY_QR;
            case UNION_QR -> UmfpayPayMethod.UNION_QR;
            // 微信公众号支付(publicnumber_and_verticalcode, GET 跳转链接)
            case WECHAT_JSAPI -> UmfpayPayMethod.WECHAT_PUBLIC;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.umfpay.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(UmfpayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 联动优势无独立通道订单控制号(order_id 即商户单号), 下单响应不回写通道单号
                // 下单恒异步受理
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 UmfpayPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
