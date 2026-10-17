package cn.daxpay.open.channel.shouqianba.service.payment.pay;

import cn.daxpay.open.channel.shouqianba.client.ShouqianbaChannelClient;
import cn.daxpay.open.channel.shouqianba.client.credential.ShouqianbaSdkCredential;
import cn.daxpay.open.channel.shouqianba.client.enums.ShouqianbaPayMethod;
import cn.daxpay.open.channel.shouqianba.client.req.ShouqianbaPayReq;
import cn.daxpay.open.channel.shouqianba.client.resp.ShouqianbaPayResp;
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

/// # 收钱吧支付执行业务服务
///
/// 通过 [ShouqianbaChannelClient] 调用子应用 dax-pay-channel-two 完成收钱吧下单。
/// 收钱吧按 payway 分渠道出码(支付宝/微信/云闪付扫码), 微信 WAP 场景走 wap api pro 收银台跳转。
@Slf4j
@Service
@RequiredArgsConstructor
public class ShouqianbaPayService {

    private final ShouqianbaChannelClient shouqianbaChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;

    /// 执行收钱吧支付
    public PayTradeResultBo pay(PayTrade order, NormalPayParam payParam, ShouqianbaSdkCredential credential) {
        // 构建请求
        ShouqianbaPayReq req = new ShouqianbaPayReq();
        // 使用支付交易号作为 client_sn 透传给收钱吧, 回调时凭此反查 PayTrade
        req.setOutTradeNo(order.getTradeNo());
        req.setAmount(payParam.getAmount());
        req.setTitle(payParam.getTitle());
        req.setClientIp(payParam.getClientIp());
        req.setNotifyUrl(this.buildNotifyUrl(order, payParam.getChannelMchNo()));
        req.setCredential(credential);
        // 支付方式映射(决定 payway 与下单形态)
        req.setMethod(this.checkMethod(payParam.getMethod()));

        // 调用子应用
        DaxResult<ShouqianbaPayResp> result = shouqianbaChannelClient.pay(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.shouqianba.payFailed", result.getMsg());
        }

        return toPayResult(result.getData());
    }

    /// 生成收钱吧支付异步通知地址(收钱吧→平台, notify_url)
    ///
    /// 路径约定: `{backendBaseUrl}/unipay/callback/{mchNo}/{channelMchNo}/shouqianba/pay`
    private String buildNotifyUrl(PayTrade order, String channelMchNo) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new BizInfoException(DaxPayErrorCode.CONFIG_ERROR, "error.common.backendBaseUrlNotConfigured");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/shouqianba/pay",
                base, order.getMchNo(), channelMchNo);
    }

    /// 平台支付方式([PayMethodEnum] code)校验与映射
    ///
    /// 收钱吧按渠道分码下单(无聚合码模式): 支付宝/微信/云闪付扫码各出各的码, 微信 WAP 走收银台跳转;
    /// 其余方式(条码/公众号/小程序等)一期未接。
    private static ShouqianbaPayMethod checkMethod(String methodCode) {
        PayMethodEnum m = PayMethodEnum.findByCode(methodCode);
        return switch (m) {
            case ALIPAY_QR -> ShouqianbaPayMethod.ALIPAY_QR;
            case WECHAT_QR -> ShouqianbaPayMethod.WECHAT_QR;
            case UNION_QR -> ShouqianbaPayMethod.UNION_QR;
            case WECHAT_H5 -> ShouqianbaPayMethod.WECHAT_H5;
            default -> throw new BizInfoException(CommonErrorCode.UN_SUPPORTED_OPERATE,
                    "error.channel.shouqianba.unsupportedPayMethod", methodCode);
        };
    }

    /// 解析子应用响应为支付结果 BO
    private PayTradeResultBo toPayResult(ShouqianbaPayResp resp) {
        PayTradeResultBo bo = new PayTradeResultBo()
                // 扫码/WAP 均为异步支付, 下单时不完成
                .setComplete(false)
                .setPayBody(resp.getPayBody());
        // 支付内容类型映射(子应用 ShouqianbaPayBodyType → 平台 PayBodyTypeEnum)
        if (Objects.nonNull(resp.getPayBodyType())) {
            switch (resp.getPayBodyType()) {
                case QR_CODE -> bo.setPayBodyType(PayBodyTypeEnum.QR_CODE);
                case LINK -> bo.setPayBodyType(PayBodyTypeEnum.LINK);
            }
        }
        return bo;
    }
}
