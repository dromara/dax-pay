package cn.daxpay.open.channel.yisheng.service.payment.pay;

import cn.daxpay.open.channel.yisheng.client.YishengChannelClient;
import cn.daxpay.open.channel.yisheng.client.credential.YishengSdkCredential;
import cn.daxpay.open.channel.yisheng.client.enums.YishengPayMethod;
import cn.daxpay.open.channel.yisheng.client.req.YishengPayReq;
import cn.daxpay.open.channel.yisheng.client.resp.YishengPayResp;
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

/// # 易生支付执行业务服务
///
/// 通过 [YishengChannelClient] 调用子应用 dax-pay-channel-two 完成易生网关下单。
/// 易生按 pay_type 区分底层渠道与形态: aliPay/wxNative(扫码)、aliJsPay/wxJsPay(JSAPI)。
/// 易生无需微信 appid(参考实现 jsapi 下单不使用 appid 参数), 微信 JS 场景仅上送 open_id。
@Slf4j
@Service
@RequiredArgsConstructor
public class YishengPayService {

    private final YishengChannelClient yishengChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行易生支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, YishengSdkCredential credential) {
        // 平台支付方式 → 易生两要素(method + payType)
        MethodMapping mapping = mapMethod(payParam.getMethod());

        // 构建请求
        YishengPayReq req = new YishengPayReq();
        // 使用支付交易号作为商户订单号透传给易生(out_trade_no), 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setMethod(mapping.method);
        req.setPayType(mapping.payType);
        if (mapping.method == YishengPayMethod.JSAPI) {
            req.setOpenId(payParam.getOpenId());
        }
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);

        // 调用子应用
        DaxResult<YishengPayResp> result = yishengChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.yisheng.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成易生支付异步通知地址(易生→平台)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/yisheng/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/yisheng/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code)校验与映射
    ///
    /// 易生支持: 支付宝扫码(aliPay)/微信扫码(wxNative)/支付宝 JS(aliJsPay)/微信 JS(wxJsPay);
    /// 其余方式(条码/H5/小程序等)一期未接。
    private static MethodMapping mapMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case ALIPAY_QR -> new MethodMapping(YishengPayMethod.QRCODE, "aliPay");
            case WECHAT_QR -> new MethodMapping(YishengPayMethod.QRCODE, "wxNative");
            case ALIPAY_JSAPI -> new MethodMapping(YishengPayMethod.JSAPI, "aliJsPay");
            case WECHAT_JSAPI -> new MethodMapping(YishengPayMethod.JSAPI, "wxJsPay");
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.yisheng.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(YishengPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 易生订单号(下单响应 order_no), 供查单/对账
                .setOutOrderNo(resp.getOutOrderNo())
                // 下单恒为异步支付, 等待通知确认
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 YishengPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case JSAPI -> bo.setPayBodyType(PayBodyTypeEnum.JSAPI);
                case IDENTIFIER -> bo.setPayBodyType(PayBodyTypeEnum.IDENTIFIER);
            }
        }
        return bo;
    }

    /// 支付方式映射内部封装
    private record MethodMapping(YishengPayMethod method, String payType) {
    }
}
