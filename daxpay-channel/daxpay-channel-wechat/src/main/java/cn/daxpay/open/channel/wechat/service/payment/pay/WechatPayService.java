package cn.daxpay.open.channel.wechat.service.payment.pay;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.enums.WechatPayBodyType;
import cn.daxpay.open.channel.wechat.client.enums.WechatPayMethod;
import cn.daxpay.open.channel.wechat.client.req.WechatPayReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
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

/// # 微信支付执行业务服务
///
/// 通过 [WechatChannelClient] 调用子应用 dax-pay-channel-one 完成微信支付下单。
/// 请求构建、响应解析全部在本类中完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatPayService {

    private final WechatChannelClient wechatChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行微信支付
    ///
    /// @param order      支付订单
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(密钥/证书)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, WechatSdkCredential credential) {
        // 构建请求
        WechatPayReq req = new WechatPayReq();
        // 使用支付交易号作为商户订单号透传给微信, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        // 微信商品描述使用支付标题
        req.setDescription(payParam.getTitle());
        // 将平台支付方式(PayMethodEnum code)映射为微信通道支付方式
        req.setMethod(mapMethod(payParam.getMethod()));
        // 付款码(MICROPAY) / 买家标识(JSAPI/MINI) 通道专属参数透传
        req.setAuthCode(payParam.getAuthCode());
        req.setOpenId(payParam.getOpenId());
        req.setAttach(payParam.getAttach());
        // 通道通知地址: 始终使用平台生成的回调地址(微信→平台), 不使用 payParam.notifyUrl(语义为平台→商户)
        req.setNotifyUrl(this.buildNotifyUrl(order));
        // 关单时间取自订单(createOrder 已对 null 兜底默认30分钟), 不用 payParam 原始入参
        req.setExpireTime(payParam.getExpiredTime());
        // H5 场景信息(场景参数)
        if (req.getMethod() == WechatPayMethod.H5) {
            req.setWapUrl(payParam.getReturnUrl());
            req.setWapName(payParam.getTitle());
        }
        req.setCredential(credential);

        // 调用子应用
        DaxResult<WechatPayResp> result = wechatChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.wechat.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成微信支付异步通知地址(微信→平台)
    ///
    /// 沿用商业版旧版路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/wechat/pay`
    /// backendBaseUrl 来自平台端点配置 [PlatformUrlConfig], 与社交登录回调地址同源
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // backendBaseUrl 未配置时抛清晰异常, 避免 null 透传到微信报模糊的必填校验错误
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/wechat/pay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) -> 微信通道支付方式
    private static WechatPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case WECHAT_QR -> WechatPayMethod.NATIVE;
            case WECHAT_JSAPI -> WechatPayMethod.JSAPI;
            case WECHAT_MINI -> WechatPayMethod.MINI;
            case WECHAT_APP -> WechatPayMethod.APP;
            case WECHAT_H5 -> WechatPayMethod.H5;
            case WECHAT_BARCODE -> WechatPayMethod.MICROPAY;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.wechat.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(WechatPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTransactionId())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 WechatPayBodyType -> 平台 PayBodyTypeEnum)
        WechatPayBodyType bodyType = resp.getPayBodyType();
        if (bodyType != null) {
            switch (bodyType) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case APP_ORDER_STR, IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 完成时间(MICROPAY 同步成功时返回)
        bo.setFinishTime(resp.getFinishTime());
        // 金额(MICROPAY 同步成功时返回)
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setRealAmount(resp.getPayerTotal());
        bo.setBuyerPayAmount(resp.getPayerTotal());
        // 用户标识
        bo.setBuyerId(resp.getOpenId());
        return bo;
    }
}
