package cn.daxpay.open.channel.adapay.service.payment.refund;

import cn.daxpay.open.channel.adapay.client.AdapayChannelClient;
import cn.daxpay.open.channel.adapay.client.credential.AdapaySdkCredential;
import cn.daxpay.open.channel.adapay.client.req.AdapayRefundReq;
import cn.daxpay.open.channel.adapay.client.resp.AdapayRefundResp;
import cn.daxpay.open.channel.adapay.code.AdapayCode;
import cn.daxpay.open.channel.adapay.util.AdapayDateUtil;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Adapay 退款业务服务
///
/// 通过 [AdapayChannelClient] 调用子应用发起Adapay 退款。
/// 退款需用原Adapay 支付对象 ID(从原 PayTrade.outOrderNo 读取)。
@Slf4j
@Service
@RequiredArgsConstructor
public class AdapayRefundService {

    private final AdapayChannelClient adapayChannelClient;
    private final PlatformUrlConfigService platformUrlConfigService;
    private final PayTradeManager payTradeManager;

    /// 执行Adapay 退款
    public RefundResultBo refund(RefundOrder refundOrder, AdapaySdkCredential credential) {
        AdapayRefundReq req = new AdapayRefundReq();
        req.setOutTradeNo(refundOrder.getTradeNo());
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setRefundAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));
        req.setCredential(credential);

        // Adapay 退款需要原支付对象 ID(从原交易读取)
        payTradeManager.findByTradeNo(refundOrder.getTradeNo())
                .ifPresentOrElse(
                        t -> req.setPaymentId(t.getOutOrderNo()),
                        () -> log.error("Adapay 退款未查到原交易({}), paymentId 未填充, 退款将失败",
                                refundOrder.getTradeNo()));

        DaxResult<AdapayRefundResp> result = adapayChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("Adapay 通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toRefundResult(result.getData());
    }

    /// 生成Adapay 退款异步通知地址
    private String buildRefundNotifyUrl(RefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (StrUtil.isBlank(base)) {
            throw new IllegalStateException("平台后端访问地址(backendBaseUrl)未配置, 无法生成Adapay 退款回调地址");
        }
        return StrUtil.format("{}/unipay/callback/{}/{}/adapay/refund",
                base, refundOrder.getMchNo(), refundOrder.getChannelMchNo());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(AdapayRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRefundNo());
        bo.setFinishTime(AdapayDateUtil.parse(resp.getFinishTime()));
        // SUCCESS 退款即时成功; PROCESSING/FAIL 需同步查询确认
        if (AdapayCode.REFUND_STATUS_SUCCESS.equals(resp.getRefundStatus())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
