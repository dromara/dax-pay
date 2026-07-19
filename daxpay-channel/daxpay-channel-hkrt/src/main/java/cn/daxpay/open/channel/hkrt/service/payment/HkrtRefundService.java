package cn.daxpay.open.channel.hkrt.service.payment;

import cn.daxpay.open.channel.hkrt.client.HkrtChannelClient;
import cn.daxpay.open.channel.hkrt.client.credential.HkrtSdkCredential;
import cn.daxpay.open.channel.hkrt.client.req.HkrtRefundReq;
import cn.daxpay.open.channel.hkrt.client.resp.HkrtRefundResp;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.common.result.DaxResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 海科融通服务商退款执行业务服务
///
/// 通过 [HkrtChannelClient] 调用子应用完成海科融通退款。
@Slf4j
@Service
@RequiredArgsConstructor
public class HkrtRefundService {

    private final HkrtChannelClient hkrtChannelClient;
    private final HkrtRefundSyncService hkrtRefundSyncService;

    /// 执行退款
    public RefundResultBo refund(RefundOrder refundOrder, HkrtSdkCredential credential) {
        HkrtRefundReq req = new HkrtRefundReq();
        req.setCredential(credential);
        // 退款单号(作为海科融通 out_refund_no)
        req.setOutRefundNo(refundOrder.getRelationOrderNo());
        // 原支付订单号(平台 tradeNo 作为原 out_trade_no)
        req.setOriginOutTradeNo(refundOrder.getTradeNo());
        // 原通道交易号(退款同步查询用, 首次退款时可能为空)
        req.setOriginTradeNo(refundOrder.getOutOrderNo());
        req.setAmount(refundOrder.getAmount());
        req.setReason(refundOrder.getReason());
        req.setClientIp(refundOrder.getClientIp());

        DaxResult<HkrtRefundResp> result = hkrtChannelClient.refund(req);
        if (result.getCode() != 0) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.hkrt.refundFailed", result.getMsg());
        }

        HkrtRefundResp resp = result.getData();
        RefundResultBo bo = new RefundResultBo()
                .setOutRefundNo(resp.getTradeNo())
                .setFinishTime(resp.getFinishTime())
                .setComplete(Boolean.TRUE.equals(resp.getComplete()));
        // 退款状态(子应用统一输出抽象态, 屏蔽海科数字码)
        String tradeStatus = resp.getTradeStatus();
        if ("FAIL".equals(tradeStatus)) {
            // 退款失败
            bo.setStatus(RefundOrderStatusEnum.FAIL);
            throw new BizInfoException(DaxPayErrorCode.TRADE_FAIL, "error.channel.hkrt.refundFailed",
                    "refund trade_status=FAIL");
        }
        bo.setStatus(Boolean.TRUE.equals(resp.getComplete())
                ? RefundOrderStatusEnum.SUCCESS
                : RefundOrderStatusEnum.PROGRESS);
        // 退款同步成功但无完成时间(海科退款接口不返回 end_time), 立即补查 refund-query 拿 end_time,
        // 避免订单进 SUCCESS 终态后被退款同步跳过导致 finishTime 永久为空
        if (bo.isComplete() && bo.getFinishTime() == null) {
            RefundResultBo syncBo = hkrtRefundSyncService.sync(refundOrder, credential);
            if (syncBo.getFinishTime() != null) {
                bo.setFinishTime(syncBo.getFinishTime());
            }
        }
        return bo;
    }
}
