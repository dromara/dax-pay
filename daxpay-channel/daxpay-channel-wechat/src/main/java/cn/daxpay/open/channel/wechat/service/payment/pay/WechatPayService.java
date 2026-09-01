package cn.daxpay.open.channel.wechat.service.payment.pay;

import cn.daxpay.open.channel.wechat.client.WechatChannelClient;
import cn.daxpay.open.channel.wechat.client.credential.WechatSdkCredential;
import cn.daxpay.open.channel.wechat.client.enums.WechatPayBodyType;
import cn.daxpay.open.channel.wechat.client.enums.WechatPayMethod;
import cn.daxpay.open.channel.wechat.client.req.WechatPayReq;
import cn.daxpay.open.channel.wechat.client.resp.WechatPayResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.PayTradeResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayParam;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.channel.PayMethodEnum;
import cn.daxpay.open.platform.core.enums.unipay.PayBodyTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.ChannelResultUnknownException;
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

    /// 微信 h5_info.app_url(wap_url) 最长128字符
    private static final int WECHAT_WAP_URL_MAX = 128;
    /// 微信 h5_info.app_name(wap_name) 最长64字符
    private static final int WECHAT_WAP_NAME_MAX = 64;
    /// 微信 attach 最长128字符(回调需原样返回, 超长报错不截断)
    private static final int WECHAT_ATTACH_MAX = 128;

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
        // attach 回调需原样返回不可截断, 平台允许500但微信限128, 超长直接报错
        if (StrUtil.length(payParam.getAttach()) > WECHAT_ATTACH_MAX) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.attachTooLong", StrUtil.length(payParam.getAttach()));
        }
        // 通道通知地址: 始终使用平台生成的回调地址(微信→平台), 不使用 payParam.notifyUrl(语义为平台→商户)
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        // 关单时间取自订单(createOrder 已对 null 兜底默认30分钟), 不用 payParam 原始入参
        req.setExpireTime(payParam.getExpiredTime());
        // H5 场景信息(场景参数)
        if (req.getMethod() == WechatPayMethod.H5) {
            // 用户终端IP(H5必填): 上游入口(NormalPayService/CashierPayService 等)已对 clientIp 做兜底提取
            req.setPayerClientIp(payParam.getClientIp());
            // wap_url(H5必填): 发起H5支付的页面地址, 优先商户returnUrl, 为空回退平台支付网关地址(收银台域名)
            req.setWapUrl(this.buildWapUrl(payParam.getReturnUrl()));
            // wap_name(纯展示字段): 平台标题最长100, 微信限64, 超长安全截断
            req.setWapName(StrUtil.subPre(payParam.getTitle(), WECHAT_WAP_NAME_MAX));
        }
        // 分账订单标识: 透传微信 profit_sharing=true
        req.setAllocation(Boolean.TRUE.equals(payParam.getAllocation()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<WechatPayResp> result = wechatChannelClient.pay(req);
        // 子应用返回"结果未知"(用户支付中/付款码已使用/订单已支付): 保持 PROCESSING 交由同步纠正,
        // 避免误判 FAIL 导致"资金已动但订单失败"悬挂
        if (result.getCode() == ChannelResultUnknownException.RESULT_UNKNOWN_CODE) {
            throw new ChannelResultUnknownException("pay.error.channelResultUnknown",
                    new RuntimeException(result.getMsg()));
        }
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.wechat.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成微信支付异步通知地址(微信→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/wechat/pay`
    /// channelMchNo 编码到 URL, 回调时 body 加密无法先反查订单, 须凭 path 组装凭证验签
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // backendBaseUrl 未配置时抛清晰异常, 避免 null 透传到微信报模糊的必填校验错误
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/wechat/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 构建H5场景地址(wap_url): 发起H5支付的页面URL
    ///
    /// 优先商户指定的returnUrl, 未传时回退平台支付网关地址(收银台模式下发起页即收银台域名,
    /// 该域名需在微信商户平台H5支付场景中登记); 微信限128字符, 超长报错不截断
    private String buildWapUrl(String returnUrl) {
        String wapUrl = StrUtil.blankToDefault(returnUrl, this.getPaymentGatewayBaseUrl());
        if (StrUtil.length(wapUrl) > WECHAT_WAP_URL_MAX) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR,
                    "error.channel.wechat.h5WapUrlTooLong", StrUtil.length(wapUrl));
        }
        return wapUrl;
    }

    /// 平台支付网关地址(兜底 wap_url 用), 未配置时抛清晰异常
    private String getPaymentGatewayBaseUrl() {
        String base = platformUrlConfigService.getUrlConfig().getPaymentGatewayBaseUrl();
        if (StrUtil.isBlank(base)) {
            // paymentGatewayBaseUrl 未配置且未传returnUrl时抛清晰异常, 与 buildNotifyUrl 对 backendBaseUrl 的处理对齐
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.h5SceneUrlNotConfigured");
        }
        return base;
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
