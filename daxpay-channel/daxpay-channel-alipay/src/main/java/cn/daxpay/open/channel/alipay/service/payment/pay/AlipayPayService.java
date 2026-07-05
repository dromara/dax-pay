package cn.daxpay.open.channel.alipay.service.payment.pay;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.enums.AlipayPayBodyType;
import cn.daxpay.open.channel.alipay.client.enums.AlipayPayMethod;
import cn.daxpay.open.channel.alipay.client.req.AlipayPayReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayPayResp;
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

/// # 支付宝支付执行业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 完成支付宝支付下单。
/// 请求构建、响应解析全部在本类中完成。
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayPayService {

    private final AlipayChannelClient alipayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行支付宝支付
    ///
    /// @param order      支付订单
    /// @param payParam   支付参数
    /// @param credential 通道调用凭证(密钥/证书)
    /// @return 支付结果
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, AlipaySdkCredential credential) {
        // 构建请求
        AlipayPayReq req = new AlipayPayReq();
        // 使用支付交易号作为商户订单号透传给支付宝, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setSubject(payParam.getTitle());
        req.setBody(payParam.getDescription());
        // 将平台支付方式(PayMethodEnum code)映射为支付宝通道支付方式
        req.setMethod(mapMethod(payParam.getMethod()));
        // 付款码(BARCODE) / 买家标识(JSAPI) 通道专属参数透传
        req.setAuthCode(payParam.getAuthCode());
        req.setOpenId(payParam.getOpenId());
        // 通道通知地址: 始终使用平台生成的回调地址(支付宝→平台), 不使用 payParam.notifyUrl(语义为平台→商户)
        req.setNotifyUrl(this.buildNotifyUrl(order));
        // 关单时间透传, 子应用据此向支付宝设置 time_expire
        req.setExpireTime(payParam.getExpiredTime());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<AlipayPayResp> result = alipayChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.alipay.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成支付宝支付异步通知地址(支付宝→平台)
    ///
    /// 沿用商业版旧版路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/alipay`
    /// backendBaseUrl 来自平台端点配置 [PlatformUrlConfig], 与社交登录回调地址同源
    /// 注: 支付宝支付/退款共用同一回调端点(旧版设计), 无 /pay 后缀, 与微信路径不同
    private String buildNotifyUrl(PayTrade order) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            // backendBaseUrl 未配置时抛清晰异常, 避免 null 透传到支付宝导致无异步回调
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成支付宝回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/alipay",
                base, order.getMchNo(), order.getAppId());
    }

    /// 平台支付方式([PayMethodEnum] code) -> 支付宝通道支付方式
    private static AlipayPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case ALIPAY_PC -> AlipayPayMethod.PC;
            case ALIPAY_H5 -> AlipayPayMethod.WAP;
            case ALIPAY_APP -> AlipayPayMethod.APP;
            case ALIPAY_QR, ALIPAY_ORDER_QR -> AlipayPayMethod.QR;
            case ALIPAY_BARCODE -> AlipayPayMethod.BARCODE;
            case ALIPAY_JSAPI, ALIPAY_MINI -> AlipayPayMethod.JSAPI;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.alipay.notSupportMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(AlipayPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setOutOrderNo(resp.getTradeNo())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()))
                .setPayBody(resp.getPayBody());

        // 支付内容类型映射(子应用 AlipayPayBodyType -> 平台 PayBodyTypeEnum)
        AlipayPayBodyType bodyType = resp.getPayBodyType();
        if (bodyType != null) {
            switch (bodyType) {
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case ORDER_STR, IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }

        // 完成时间
        bo.setFinishTime(resp.getFinishTime());
        // 金额(BARCODE 付款码同步成功时返回)
        bo.setRealAmount(resp.getBuyerPayAmount());
        bo.setTotalAmount(resp.getTotalAmount());
        bo.setBuyerPayAmount(resp.getBuyerPayAmount());
        // 用户标识
        bo.setBuyerId(resp.getBuyerOpenId());
        bo.setUserId(resp.getBuyerUserId());
        bo.setBuyerLogonId(resp.getBuyerLogonId());
        return bo;
    }
}
