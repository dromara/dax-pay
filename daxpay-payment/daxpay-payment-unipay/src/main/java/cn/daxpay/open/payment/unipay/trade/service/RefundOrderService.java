package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundService;
import cn.daxpay.open.payment.unipay.param.trade.refund.RefundParam;
import cn.daxpay.open.payment.unipay.result.trade.refund.RefundResult;
import cn.daxpay.open.payment.unipay.trade.convert.UnipayRefundOrderConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 退款发起服务(对外)
///
/// 对外统一退款的编排入口: 对外签名入参 → 内部编排参数, 委托核心 [RefundService] 建单调通道,
/// 再将退款单实体映射为对外响应 [RefundResult]。
/// 核心层只认内部 [cn.daxpay.open.payment.trade.runtime.param.RefundParam], 对外签名字段(mchNo/appId/sign)在此剥离。
/// 与退款查询/同步的 [RefundOrderQueryService] / [RefundOrderSyncService] 并列。
@Service
@RequiredArgsConstructor
public class RefundOrderService {

    private final RefundService refundService;

    /// 发起退款
    public RefundResult refund(RefundParam param) {
        RefundOrder refundOrder = refundService.refund(UnipayRefundOrderConvert.CONVERT.toRuntime(param));
        return UnipayRefundOrderConvert.CONVERT.toRefundResult(refundOrder);
    }
}
