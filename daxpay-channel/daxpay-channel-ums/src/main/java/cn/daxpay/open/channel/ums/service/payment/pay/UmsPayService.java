package cn.daxpay.open.channel.ums.service.payment.pay;

import cn.daxpay.open.channel.ums.client.UmsChannelClient;
import cn.daxpay.open.channel.ums.client.credential.UmsSdkCredential;
import cn.daxpay.open.channel.ums.client.enums.UmsPayBodyType;
import cn.daxpay.open.channel.ums.client.enums.UmsPayMethod;
import cn.daxpay.open.channel.ums.client.req.UmsPayReq;
import cn.daxpay.open.channel.ums.client.resp.UmsPayResp;
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

/// # 银联商务支付执行业务服务
///
/// 通过 [UmsChannelClient] 调用子应用 dax-pay-channel-one 完成银联商务支付下单。
/// 银联商务为聚合支付, 扫码方式(支付宝/微信/银联扫码)统一映射为 [UmsPayMethod.QRCODE]。
@Slf4j
@Service
@RequiredArgsConstructor
public class UmsPayService {

    private final UmsChannelClient umsChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行银联商务支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, UmsSdkCredential credential) {
        // 构建请求
        UmsPayReq req = new UmsPayReq();
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setDescription(payParam.getTitle());
        // 平台支付方式映射为银联商务通道支付方式
        req.setMethod(this.mapMethod(payParam.getMethod()));
        req.setClientIp(payParam.getClientIp());
        // 通道通知地址(银联商务→平台)
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<UmsPayResp> result = umsChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.ums.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成银联商务支付异步通知地址(银联商务→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{appId}/ums/{channelMchNo}/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/ums/{}/pay",
                base, order.getMchNo(), order.getAppId(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code) -> 银联商务通道支付方式
    ///
    /// 银联商务为聚合支付, 所有扫码方式统一映射为 QRCODE;
    /// H5/JSAPI 按支付渠道(支付宝/微信/银联)分别映射。
    private static UmsPayMethod mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            // 聚合扫码(支付宝/微信/银联扫码统一走银联商务聚合扫码)
            case AGGREGATE_PAY_QRCODE, ALIPAY_QR, WECHAT_QR, UNION_QR -> UmsPayMethod.QRCODE;
            // 支付宝 H5 / PC
            case ALIPAY_H5, ALIPAY_PC -> UmsPayMethod.ALIPAY_H5;
            // 微信 H5
            case WECHAT_H5 -> UmsPayMethod.WECHAT_H5;
            // 微信小程序 / JSAPI / 收银台
            case WECHAT_JSAPI, WECHAT_MINI, WECHAT_CASHIER -> UmsPayMethod.WECHAT_CASHIER;
            // 银联 H5 / JSAPI(云闪付)
            case UNION_H5, UNION_JSAPI -> UmsPayMethod.UNION_JSAPI;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.ums.notSupportMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(UmsPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射
        UmsPayBodyType bodyType = resp.getPayBodyType();
        if (bodyType != null) {
            switch (bodyType) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
