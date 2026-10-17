package cn.daxpay.open.channel.jeepay.service.payment;

import cn.daxpay.open.channel.jeepay.client.JeepayChannelClient;
import cn.daxpay.open.channel.jeepay.client.credential.JeepaySdkCredential;
import cn.daxpay.open.channel.jeepay.client.enums.JeepayPayBodyType;
import cn.daxpay.open.channel.jeepay.client.enums.JeepayPayMethod;
import cn.daxpay.open.channel.jeepay.client.req.JeepayPayReq;
import cn.daxpay.open.channel.jeepay.client.resp.JeepayPayResp;
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

/// # Jeepay支付执行业务服务
///
/// 通过 [JeepayChannelClient] 调用子应用 dax-pay-channel-two 完成 Jeepay 统一下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 一期 12 项支付方式(扫码+JSAPI 族, 对齐 pay_md_product_capability 主数据),
/// wayCode 与 channelExtra 参数组合由子应用按 payMethod 组装;
/// Jeepay 下单为异步确认模式, 同步不返回终态。
@Slf4j
@Service
@RequiredArgsConstructor
public class JeepayPayService {

    private final JeepayChannelClient jeepayChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行 Jeepay 支付
    ///
    /// @param order      支付订单(tradeNo 作为商户订单号)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(密钥配置 + 平台身份)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, JeepaySdkCredential credential) {
        // 平台支付方式 → Jeepay 支付方式(12 项白名单校验)
        JeepayPayMethod payMethod = mapMethod(payParam.getMethod());

        // 构建请求
        JeepayPayReq req = new JeepayPayReq();
        // 使用支付交易号作为商户订单号透传给 Jeepay, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setPayMethod(payMethod);
        // 商品标题(作为 Jeepay subject/body 字段): 优先描述, 为空回退标题
        req.setTitle(StrUtil.blankToDefault(payParam.getDescription(), payParam.getTitle()));
        // jsapi 族(微信公众号/小程序/支付宝生活号)必填用户标识, 子应用凭此组装 channelExtra
        if (payMethod.requiresOpenId()) {
            if (StrUtil.isBlank(payParam.getOpenId())) {
                // Jeepay 不支持的支付方式入参(jsapi 族缺 openId)
                throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                        "error.channel.jeepay.missingOpenId", payMethod.getCode());
            }
            req.setOpenId(payParam.getOpenId());
        }
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<JeepayPayResp> result = jeepayChannelClient.pay(req);
        if (result.getCode() != 0) {
            // Jeepay 支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.jeepay.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成 Jeepay 支付异步通知地址(Jeepay 平台 → 平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/jee_pay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/jee_pay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] code) → Jeepay 支付方式(12 项白名单)
    private static JeepayPayMethod mapMethod(String methodCode) {
        JeepayPayMethod method = JeepayPayMethod.findByCodeOrNull(methodCode);
        if (Objects.isNull(method)) {
            // Jeepay 不支持的支付方式
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.jeepay.unsupportedPayMethod", methodCode);
        }
        return method;
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(JeepayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 JeepayPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case FROM -> bo.setPayBodyType(PayBodyTypeEnum.FROM);
            }
        }
        return bo;
    }
}
