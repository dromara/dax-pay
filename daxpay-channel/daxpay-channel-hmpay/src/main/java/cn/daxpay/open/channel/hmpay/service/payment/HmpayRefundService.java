package cn.daxpay.open.channel.hmpay.service.payment;

import cn.daxpay.open.channel.hmpay.client.HmpayChannelClient;
import cn.daxpay.open.channel.hmpay.client.credential.HmpaySdkCredential;
import cn.daxpay.open.channel.hmpay.client.req.HmpayRefundReq;
import cn.daxpay.open.channel.hmpay.client.resp.HmpayRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/// # 河马付服务商退款执行业务服务
///
/// 通过 [HmpayChannelClient] 调用子应用完成河马付(杉德)退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class HmpayRefundService {

    /// 杉德纯日期时间格式(yyyyMMddHHmmss)
    private static final DateTimeFormatter PURE_DATETIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final HmpayChannelClient hmpayChannelClient;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, HmpaySdkCredential credential) {
        HmpayRefundReq req = new HmpayRefundReq();
        req.setCredential(credential);
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原商户订单号(支付时下送给杉德的 out_order_no, 即平台支付交易号)
        req.setOutTradeNo(refundOrder.getTradeNo());
        // 原支付下单时间(杉德 order_create_time, 取退款单创建时间近似, 杉德主要靠 out_order_no 定位)
        req.setOrderCreateTime(formatPureDateTime(refundOrder.getCreateTime()));
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());

        DaxResult<HmpayRefundResp> result = hmpayChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "channel.error.hmpayRefundFailed", result.getMsg());
        }

        HmpayRefundResp resp = result.getData();
        boolean complete = Boolean.TRUE.equals(resp.getComplete());
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(complete);
        // 退款状态: 同步成功(refund_success_time 非空) 视为 SUCCESS, 否则处理中
        bo.setStatus(complete ? RefundOrderStatusEnum.SUCCESS : RefundOrderStatusEnum.PROGRESS);
        return bo;
    }

    /// OffsetDateTime → yyyyMMddHHmmss(东八区)
    private String formatPureDateTime(OffsetDateTime time) {
        return time == null ? null : time.toLocalDateTime().format(PURE_DATETIME);
    }
}
