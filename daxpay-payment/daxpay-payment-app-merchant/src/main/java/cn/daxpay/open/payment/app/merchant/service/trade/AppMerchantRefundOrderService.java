package cn.daxpay.open.payment.app.merchant.service.trade;

import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.order.service.RefundOrderService;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 商户移动端-退款订单服务
///
/// 转发至 core [RefundOrderService]
@Service
@RequiredArgsConstructor
public class AppMerchantRefundOrderService {

    private final RefundOrderService refundOrderService;

    /// 分页查询
    public PageResult<RefundOrderResult> page(PageParam pageParam, RefundOrderQuery query) {
        return refundOrderService.page(pageParam, query);
    }

    /// 详情查询
    public RefundOrderResult findById(Long id) {
        return refundOrderService.findById(id);
    }
}
