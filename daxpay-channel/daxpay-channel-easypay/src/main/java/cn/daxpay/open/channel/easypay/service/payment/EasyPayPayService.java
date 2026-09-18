package cn.daxpay.open.channel.easypay.service.payment;

import cn.daxpay.open.channel.easypay.client.EasyPayChannelClient;
import cn.daxpay.open.channel.easypay.client.credential.EasyPaySdkCredential;
import cn.daxpay.open.channel.easypay.client.enums.EasyPayPayBodyType;
import cn.daxpay.open.channel.easypay.client.enums.EasyPayPayMethod;
import cn.daxpay.open.channel.easypay.client.req.EasyPayPayReq;
import cn.daxpay.open.channel.easypay.client.resp.EasyPayPayResp;
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

/// # 易支付支付执行业务服务
///
/// 通过 [EasyPayChannelClient] 调用子应用 dax-pay-channel-two 完成易支付统一下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 一期仅扫码两类(ALIPAY_QR/WECHAT_QR), method/device/type 参数组合由子应用按 payMethod 组装;
/// 易支付下单为异步确认模式, 同步不返回终态。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayPayService {

    private final EasyPayChannelClient easyPayChannelClient;

    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行易支付支付
    ///
    /// @param order      支付订单(tradeNo 作为商户订单号)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(密钥配置 + 平台身份)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, EasyPaySdkCredential credential) {
        // 平台支付方式 → 易支付支付方式(一期两类白名单校验)
        EasyPayPayMethod payMethod = mapMethod(payParam.getMethod());

        // 构建请求
        EasyPayPayReq req = new EasyPayPayReq();
        // 使用支付交易号作为商户订单号透传给易支付, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setPayMethod(payMethod);
        // 商品标题(作为易支付 name 字段): 优先描述, 为空回退标题
        req.setTitle(StrUtil.blankToDefault(payParam.getDescription(), payParam.getTitle()));
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<EasyPayPayResp> result = easyPayChannelClient.pay(req);
        if (result.getCode() != 0) {
            // 易支付支付异常
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.easypay.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成易支付支付异步通知地址(易支付平台 → 平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/easy_pay/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // 后端服务地址未配置
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/easy_pay/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum] code) → 易支付支付方式(一期两类白名单)
    private static EasyPayPayMethod mapMethod(String methodCode) {
        EasyPayPayMethod method = EasyPayPayMethod.findByCodeOrNull(methodCode);
        if (Objects.isNull(method)) {
            // 易支付不支持的支付方式(含一期未接入的 jsapi 族)
            throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.easypay.unsupportedPayMethod", methodCode);
        }
        return method;
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(EasyPayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 EasyPayPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case FROM -> bo.setPayBodyType(PayBodyTypeEnum.FROM);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
                case JSON -> bo.setPayBodyType(PayBodyTypeEnum.JSON);
            }
        }
        return bo;
    }
}
