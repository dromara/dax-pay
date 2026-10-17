package cn.daxpay.open.channel.helipay.service.payment;

import cn.daxpay.open.channel.helipay.client.HelipayChannelClient;
import cn.daxpay.open.channel.helipay.client.credential.HelipaySdkCredential;
import cn.daxpay.open.channel.helipay.client.enums.HelipayPayBodyType;
import cn.daxpay.open.channel.helipay.client.enums.HelipayPayMethod;
import cn.daxpay.open.channel.helipay.client.req.HelipayPayReq;
import cn.daxpay.open.channel.helipay.client.resp.HelipayPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 合利宝支付执行业务服务
///
/// 通过 [HelipayChannelClient] 调用子应用 dax-pay-channel-two 完成合利宝下单
/// (AppPay 扫码 / AppPayPublic 公众号 / AppPayH5WFT H5 三条业务形态)。
/// 一期五项支付方式(对齐 pay_md_product_capability 主数据):
/// alipay_qr / wechat_qr(扫码), alipay_jsapi / wechat_jsapi(公众号/生活号, 需 openId), wechat_h5(H5)。
///
/// 金额单位: 平台内部「分」, 合利宝接口「元」, 子应用负责换算;
/// 下单为异步确认模式, 同步不返回终态。
@Slf4j
@Service
@RequiredArgsConstructor
public class HelipayPayService {

    private final HelipayChannelClient helipayChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行合利宝支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, HelipaySdkCredential credential) {
        // 构建请求
        HelipayPayReq req = new HelipayPayReq();
        // 使用支付交易号作为商户订单号透传给合利宝(P2_orderId), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setPayMethod(this.mapMethod(payParam.getMethod()));
        // 公众号 JS 族必填用户标识(微信 openid / 支付宝 buyerId)
        boolean jsapiFamily = req.getPayMethod() == HelipayPayMethod.ALIPAY_JSAPI
                || req.getPayMethod() == HelipayPayMethod.WECHAT_JSAPI;
        if (jsapiFamily && StrUtil.isBlank(payParam.getOpenId())) {
            // 合利宝JS支付必填openId
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.helipay.missingOpenId", payParam.getMethod());
        }
        req.setOpenId(payParam.getOpenId());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<HelipayPayResp> result = helipayChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 合利宝支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.helipay.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成合利宝支付异步通知地址(合利宝 → 平台, P9_notifyUrl)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/helipay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/helipay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → 合利宝支付方式(五项白名单)
    private static HelipayPayMethod mapMethod(String methodCode) {
        HelipayPayMethod method;
        try {
            method = HelipayPayMethod.valueOf(methodCode.toUpperCase());
        } catch (IllegalArgumentException e) {
            method = null;
        }
        if (Objects.isNull(method)) {
            // 合利宝不支持的支付方式
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.helipay.unsupportedPayMethod", methodCode);
        }
        return method;
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(HelipayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 合利宝流水号回写, 供后续查单/退款
                .setOutOrderNo(resp.getOutOrderNo())
                // 下单为异步确认, 同步不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 HelipayPayBodyType → 平台 PayBodyTypeEnum)
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
