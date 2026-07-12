package cn.daxpay.open.channel.alipay.service.payment.refund;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayRefundSyncReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayRefundSyncResp;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝退款同步业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 查询支付宝退款状态,
/// 将支付宝 refund_status 映射为平台 [RefundOrderStatusEnum]。
///
/// 映射规则:
/// - REFUND_SUCCESS → SUCCESS(退款成功)
/// - 其他 → PROGRESS(退款中)
/// - 查询失败 → syncSuccess=false
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayRefundSyncService {

    private final AlipayChannelClient alipayChannelClient;

    /// 退款成功状态码
    private static final String REFUND_SUCCESS = "REFUND_SUCCESS";

    /// 执行支付宝退款同步查询
    ///
    /// @param refundOrder 退款订单(refundNo 作为 out_request_no, orderNo 作为 out_trade_no)
    /// @param credential  通道调用凭证
    /// @return 同步结果(含映射后的退款状态)
    public RefundResultBo sync(PayRefundOrder refundOrder, AlipaySdkCredential credential) {
        // 构建请求
        var req = new AlipayRefundSyncReq();
        req.setOutTradeNo(refundOrder.getOrderNo());
        req.setTradeNo(refundOrder.getOutOrderNo());
        req.setOutRequestNo(refundOrder.getRefundNo());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<AlipayRefundSyncResp> result = alipayChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("支付宝通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            RefundResultBo bo = new RefundResultBo();
            bo.setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
            return bo;
        }

        return toSyncResult(result.getData());
    }

    /// 解析子应用响应, 映射 refund_status → [RefundOrderStatusEnum]
    private RefundResultBo toSyncResult(AlipayRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRequestNo());

        // REFUND_SUCCESS → 退款成功
        if (REFUND_SUCCESS.equals(resp.getRefundStatus())) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS)
                    .setFinishTime(resp.getFinishTime())
                    .setRefundAmount(resp.getRefundAmount());
        }

        // 未查询到或处理中
        return bo.setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS)
                .setSyncSuccess(StrUtil.isBlank(resp.getSubCode()));
    }
}
