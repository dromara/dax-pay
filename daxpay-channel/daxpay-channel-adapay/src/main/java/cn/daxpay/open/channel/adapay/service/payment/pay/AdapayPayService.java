package cn.daxpay.open.channel.adapay.service.payment.pay;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.enums.AdapayPayBodyType;
import cn.daxpay.open.channel.adapay.client.enums.AdapayPayMethod;
import cn.daxpay.open.channel.adapay.client.req.AdapayPayReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayPayResp;
import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.system.service.config.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 汇付天下支付执行业务服务
///
/// 通过 [AdapayChannelClient] 调用子应用 dax-pay-channel-two 完成汇付天下支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 汇付为聚合通道: 一个接口承载微信/支付宝/银联三种底层渠道, 由 pay_channel 决定路由。
/// 支付方式枚举 name 与平台 [PayMethodEnum] 对齐, 直接 valueOf 完成映射。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayPayService {

    private final AdapayChannelClient adapayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行汇付天下支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, AdapaySdkCredential credential) {
        // 构建请求
        AdapayPayReq req = new AdapayPayReq();
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        // 平台支付方式 → 汇付通道支付方式(枚举 name 对齐, 直接 valueOf)
        req.setMethod(this.mapMethod(payParam.getMethod()));
        req.setOpenId(payParam.getOpenId());
        req.setAuthCode(payParam.getAuthCode());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<AdapayPayResp> result = adapayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.adapayPayFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成汇付天下支付异步通知地址(汇付→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/adapay/pay`
    /// 汇付回调验签只需平台公钥(全局固定), 路径不带 channelMchNo, 凭 order_no 反查 PayTrade。
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/adapay/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) → 汇付通道支付方式
    ///
    /// 汇付 [AdapayPayMethod] 枚举 name 与 [PayMethodEnum] 对齐, 直接按 name 映射;
    /// 不在汇付支持列表的方式(如 WECHAT_CASHIER/ALIPAY_MINI/抖音/Visa 等)抛不支持异常。
    private static AdapayPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        try {
            return AdapayPayMethod.valueOf(m.name());
        } catch (IllegalArgumentException e) {
            // 汇付: 不支持的支付方式
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "channel.error.adapayUnsupportedPayMethod", methodCode);
        }
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(AdapayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 汇付支付对象 ID, 后续 sync/close/refund 的关键凭证
                .setOutOrderNo(resp.getPaymentId())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射
        if (resp.getPayBodyType() != null) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 条码支付同步成功时的金额与时间
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getRealAmount());
        bo.setBuyerId(resp.getBuyerId());
        return bo;
    }
}
