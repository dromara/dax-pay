package cn.daxpay.open.channel.leshua.service.payment;

import cn.daxpay.open.channel.leshua.client.LeshuaChannelClient;
import cn.daxpay.open.channel.leshua.client.credential.LeshuaSdkCredential;
import cn.daxpay.open.channel.leshua.client.req.LeshuaRefundReq;
import cn.daxpay.open.channel.leshua.client.resp.LeshuaRefundResp;
import cn.daxpay.open.channel.leshua.code.LeshuaCode;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 乐刷服务商退款执行业务服务
///
/// 通过 [LeshuaChannelClient] 调用子应用完成乐刷退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class LeshuaRefundService {

    private final LeshuaChannelClient leshuaChannelClient;
    private final cn.daxpay.open.platform.system.service.config.infra.PlatformUrlConfigService platformUrlConfigService;

    /// 执行退款
    public RefundResultBo refund(PayRefundOrder refundOrder, LeshuaSdkCredential credential) {
        LeshuaRefundReq req = new LeshuaRefundReq();
        req.setCredential(credential);
        // 原乐刷订单号(原支付 leshua_order_id)
        req.setLeshuaOrderId(refundOrder.getOutOrderNo());
        // 退款单号(作为乐刷 merchant_refund_id)
        req.setOutRefundNo(refundOrder.getRefundNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setNotifyUrl(this.buildRefundNotifyUrl(refundOrder));

        DaxResult<LeshuaRefundResp> result = leshuaChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.leshuaRefundFailed", result.getMsg());
        }

        LeshuaRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getLeshuaRefundId())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // 退款状态映射
        bo.setStatus(mapRefundStatus(resp.getRefundStatus()));
        return bo;
    }

    /// 生成乐刷退款异步通知地址
    private String buildRefundNotifyUrl(PayRefundOrder refundOrder) {
        String base = platformUrlConfigService.getUrlConfig().getBackendBaseUrl();
        if (cn.hutool.core.util.StrUtil.isBlank(base)) {
            return null;
        }
        return cn.hutool.core.util.StrUtil.format("{}/unipay/callback/{}/{}/leshua/refund",
                base, refundOrder.getMchNo(), refundOrder.getAppId());
    }

    /// 乐刷退款 status → 平台退款状态
    private RefundOrderStatusEnum mapRefundStatus(String status) {
        if (Objects.equals(status, LeshuaCode.REFUND_STATUS_SUCCESS)) {
            return RefundOrderStatusEnum.SUCCESS;
        }
        if (Objects.equals(status, LeshuaCode.REFUND_STATUS_FAIL)) {
            return RefundOrderStatusEnum.FAIL;
        }
        // 其他(退款中)
        return RefundOrderStatusEnum.PROGRESS;
    }
}
