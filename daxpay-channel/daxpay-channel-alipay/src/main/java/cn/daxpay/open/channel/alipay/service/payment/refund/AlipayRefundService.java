package cn.daxpay.open.channel.alipay.service.payment.refund;

import cn.daxpay.open.channel.alipay.client.AlipayChannelClient;
import cn.daxpay.open.channel.alipay.client.credential.AlipaySdkCredential;
import cn.daxpay.open.channel.alipay.client.req.AlipayRefundReq;
import cn.daxpay.open.channel.alipay.client.resp.AlipayRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 支付宝退款业务服务
///
/// 通过 [AlipayChannelClient] 调用子应用 dax-pay-channel-one 发起支付宝退款。
/// 请求构建、结果判定在本类中完成。
///
/// 资金变动判定:
/// - 子应用返回 `complete=true`(fund_change=Y) → 退款即时成功
/// - `complete=false`(fund_change=N) → 需退款同步查询确认最终状态
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayRefundService {

    private final AlipayChannelClient alipayChannelClient;

    /// 执行支付宝退款
    ///
    /// @param refundOrder 退款订单(refundNo 作为 out_request_no, orderNo 作为 out_trade_no, outOrderNo 作为 trade_no)
    /// @param credential  通道调用凭证
    /// @return 退款结果(含映射后的退款状态)
    public RefundResultBo refund(RefundOrder refundOrder, AlipaySdkCredential credential) {
        // 构建请求
        AlipayRefundReq req = new AlipayRefundReq();
        req.setOutTradeNo(refundOrder.getTradeNo());
        req.setTradeNo(refundOrder.getOutOrderNo());
        req.setOutRequestNo(refundOrder.getRelationOrderNo());
        req.setRefundAmount(refundOrder.getAmount());
        req.setCredential(credential);

        // 调用子应用
        DaxResult<AlipayRefundResp> result = alipayChannelClient.refund(req);
        if (result.getCode() != 0) {
            log.error("支付宝通道退款失败: refundNo={}, msg={}", refundOrder.getRefundNo(), result.getMsg());
            RefundResultBo bo = new RefundResultBo();
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.FAIL)
                    .setSyncSuccess(false)
                    .setSyncErrorMsg(result.getMsg());
            return bo;
        }

        return toRefundResult(result.getData());
    }

    /// 解析子应用响应
    private RefundResultBo toRefundResult(AlipayRefundResp resp) {
        RefundResultBo bo = new RefundResultBo();
        bo.setOutRefundNo(resp.getOutRequestNo());
        bo.setFinishTime(resp.getFinishTime());
        // fund_change=Y 退款即时成功; 否则退款中, 需同步查询确认
        if (Boolean.TRUE.equals(resp.getComplete())) {
            bo.setComplete(true)
                    .setStatus(RefundOrderStatusEnum.SUCCESS);
        } else {
            bo.setComplete(false)
                    .setStatus(RefundOrderStatusEnum.PROGRESS);
        }
        return bo;
    }
}
