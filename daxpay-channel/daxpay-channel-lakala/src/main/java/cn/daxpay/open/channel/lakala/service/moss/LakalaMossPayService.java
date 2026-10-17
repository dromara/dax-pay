package cn.daxpay.open.channel.lakala.service.moss;

import cn.daxpay.open.channel.lakala.client.LakalaChannelClient;
import cn.daxpay.open.channel.lakala.client.credential.MossSdkCredential;
import cn.daxpay.open.channel.lakala.client.enums.LakalaPayBodyType;
import cn.daxpay.open.channel.lakala.client.enums.MossPayMethod;
import cn.daxpay.open.channel.lakala.client.req.MossPayReq;
import cn.daxpay.open.channel.lakala.client.resp.MossPayResp;
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

/// # 拉卡拉 MOSS 支付执行业务服务
///
/// 通过 [LakalaChannelClient] 调用子应用 dax-pay-channel-two 完成 MOSS 支付下单。
/// 请求构建、响应解析全部在本类中完成。
///
/// 能力分支映射(对齐主数据 lakala_moss 四能力):
/// - wechat_qr → CASHIER 收银台(pay_scene=0, counter_url 渲染二维码)
/// - wechat_jsapi → PREORDER WECHAT 51(acc_resp_fields JSAPI 调起参数)
/// - alipay_qr → PREORDER ALIPAY 41(acc_resp_fields.code 二维码)
/// - alipay_jsapi → PREORDER ALIPAY 51(prepay_id 标识码)
@Slf4j
@Service
@RequiredArgsConstructor
public class LakalaMossPayService {

    private final LakalaChannelClient lakalaChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行 MOSS 支付
    ///
    /// @param order      支付订单(tradeNo 作为 order_no)
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(MOSS appid + 商户私钥 + 商户号)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, MossSdkCredential credential) {
        // 平台支付方式 → MOSS 三要素(method + accountType + transType + bodyType)
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        MossPayReq req = new MossPayReq();
        // 使用支付交易号作为商户订单号透传给 MOSS, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setDescription(payParam.getDescription());
        req.setMethod(mapping.method());
        req.setAccountType(mapping.accountType());
        req.setTransType(mapping.transType());
        req.setPayBodyType(mapping.bodyType());
        // 通道专属参数透传
        req.setOpenId(payParam.getOpenId());
        // 通道应用 AppId(微信 JSAPI sub_appid)
        req.setChannelAppId(payParam.getChannelAppId());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setReturnUrl(payParam.getReturnUrl());
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<MossPayResp> result = lakalaChannelClient.mossPay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.lakala.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成 MOSS 支付异步通知地址(MOSS→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/lakala/moss/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/lakala/moss/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) → MOSS 三要素
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 微信扫码(收银台 counter_url, 渲染为二维码)
            case WECHAT_QR -> new MethodMapping(MossPayMethod.CASHIER, "WECHAT", null, LakalaPayBodyType.QR_CODE);
            // 微信 JSAPI(acc_resp_fields JSAPI 调起参数)
            case WECHAT_JSAPI -> new MethodMapping(MossPayMethod.PREORDER, "WECHAT", "51", LakalaPayBodyType.JSAPI);
            // 支付宝扫码(acc_resp_fields.code 二维码)
            case ALIPAY_QR -> new MethodMapping(MossPayMethod.PREORDER, "ALIPAY", "41", LakalaPayBodyType.QR_CODE);
            // 支付宝 JSAPI(prepay_id 标识码)
            case ALIPAY_JSAPI -> new MethodMapping(MossPayMethod.PREORDER, "ALIPAY", "51", LakalaPayBodyType.IDENTIFIER);
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.lakala.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(MossPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 LakalaPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }
        // MOSS 下单无同步完成态, 金额/时间由回调或同步补充
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(MossPayMethod method, String accountType, String transType,
                                 LakalaPayBodyType bodyType) {
    }
}
