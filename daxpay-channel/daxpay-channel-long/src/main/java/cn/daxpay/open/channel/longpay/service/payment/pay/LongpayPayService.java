package cn.daxpay.open.channel.longpay.service.payment.pay;

import cn.daxpay.open.channel.longpay.client.LongpayChannelClient;
import cn.daxpay.open.channel.longpay.client.credential.LongpaySdkCredential;
import cn.daxpay.open.channel.longpay.client.enums.LongpayPayMethod;
import cn.daxpay.open.channel.longpay.client.req.LongpayPayReq;
import cn.daxpay.open.channel.longpay.client.resp.LongpayPayResp;
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

/// # 建行龙支付支付执行业务服务
///
/// 通过 [LongpayChannelClient] 调用子应用 dax-pay-channel-two 完成建行龙支付聚合下单。
/// 龙支付聚合通道: 一张聚合码承载微信/支付宝/银联/建行钱包主扫, 建行侧自动识别底层渠道。
///
/// 建行异步通知地址在建行商户平台侧配置(服务器实时反馈), 本服务仍按平台规范生成规范回调地址透传预留。
@Slf4j
@Service
@RequiredArgsConstructor
public class LongpayPayService {

    private final LongpayChannelClient longpayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行建行龙支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, LongpaySdkCredential credential) {
        // 构建请求
        LongpayPayReq req = new LongpayPayReq();
        // 使用支付交易号作为商户订单号透传给建行(ORDERID), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);
        // 支付方式校验(一期仅聚合扫码)
        this.checkMethod(payParam.getMethod());

        // 调用子应用
        DaxResult<LongpayPayResp> result = longpayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.long.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 平台支付方式([PayMethodEnum] code)校验
    ///
    /// 建行龙支付为聚合主扫码: 聚合扫码/微信扫码/支付宝扫码/银联扫码均落同一张聚合码;
    /// 其余方式(条码/JSAPI/H5/小程序等)待建行正式文档核对后接入。
    private static LongpayPayMethod checkMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case AGGREGATE_PAY_QRCODE, WECHAT_QR, ALIPAY_QR, UNION_QR -> LongpayPayMethod.QR;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.long.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(LongpayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 聚合扫码为异步支付, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 LongpayPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
            }
        }
        return bo;
    }
}
