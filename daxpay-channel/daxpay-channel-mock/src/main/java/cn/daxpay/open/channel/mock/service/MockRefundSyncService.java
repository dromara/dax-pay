package cn.daxpay.open.channel.mock.service;

import cn.daxpay.open.channel.mock.client.MockChannelClient;
import cn.daxpay.open.channel.mock.client.req.MockRefundSyncReq;
import cn.daxpay.open.channel.mock.client.resp.MockRefundSyncResp;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # Mock 通道退款同步业务服务
///
/// 通过 [MockChannelClient] 调用独立 mock 子应用查询 mock 退款单状态,
/// 机制对齐支付宝 [cn.daxpay.open.channel.alipay.service.payment.refund.AlipayRefundSyncService]。
///
/// 映射规则(比支付宝多一个显式 FAIL 终态, mock 退款状态机明确区分):
/// - SUCCESS → SUCCESS(退款成功)
/// - FAIL → FAIL(退款失败终态)
/// - PROCESSING → PROGRESS(退款中)
/// - 查询失败 → syncSuccess=false
@Slf4j
@Service
@RequiredArgsConstructor
public class MockRefundSyncService {

    /// mock 退款状态: 退款成功
    private static final String REFUND_SUCCESS = "SUCCESS";
    /// mock 退款状态: 退款失败
    private static final String REFUND_FAIL = "FAIL";

    private final MockChannelClient mockChannelClient;
    private final MockCredentialSupport mockCredentialSupport;

    /// 执行 mock 退款同步
    ///
    /// @param refundOrder 退款订单(relationOrderNo 作为商户退款号透传)
    /// @return 同步结果(含映射后的退款状态)
    public RefundResultBo sync(RefundOrder refundOrder) {
        // 构建请求(凭证按订单 product/通道商户号取商户级两件套)
        MockRefundSyncReq req = new MockRefundSyncReq();
        // 商户退款号 = 实际上送通道的商户退款关联号(对齐支付宝 outRequestNo 口径)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        req.setCredential(mockCredentialSupport.build(refundOrder.getProduct(), refundOrder.getChannelMchNo()));

        // 调用 mock 子应用
        DaxResult<MockRefundSyncResp> result = mockChannelClient.refundSync(req);
        if (result.getCode() != 0) {
            log.error("模拟通道退款同步失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            return new RefundResultBo()
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
        }

        return toSyncResult(result.getData());
    }

    /// 解析 mock 响应, 映射 refundStatus → [RefundOrderStatusEnum]
    private RefundResultBo toSyncResult(MockRefundSyncResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getRefundId());

        // 退款成功
        if (REFUND_SUCCESS.equals(resp.getRefundStatus())) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS)
                    .setFinishTime(resp.getFinishTime())
                    .setRefundAmount(resp.getRefundAmount());
        }

        // 退款失败终态(mock 显式返回 FAIL)
        if (REFUND_FAIL.equals(resp.getRefundStatus())) {
            return bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setFinishTime(resp.getFinishTime())
                    .setRefundAmount(resp.getRefundAmount());
        }

        // 处理中(PROCESSING)
        return bo.setComplete(false)
                .setStatus(RefundOrderStatusEnum.PROGRESS);
    }
}
