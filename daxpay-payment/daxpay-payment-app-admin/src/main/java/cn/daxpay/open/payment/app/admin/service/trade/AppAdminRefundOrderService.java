package cn.daxpay.open.payment.app.admin.service.trade;

import cn.daxpay.open.payment.admin.service.trade.RefundOrderAdminService;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 运营移动端-退款订单服务
///
/// 转发至 [RefundOrderAdminService]
@Service
@RequiredArgsConstructor
public class AppAdminRefundOrderService {

    private final RefundOrderAdminService refundOrderAdminService;

    /// 分页查询
    public PageResult<RefundOrderResult> page(PageParam pageParam, RefundOrderQuery query) {
        return refundOrderAdminService.page(pageParam, query);
    }

    /// 详情查询
    public RefundOrderResult findById(Long id) {
        return refundOrderAdminService.findById(id);
    }

    /// 发起退款
    public RefundOrderResult refund(RefundParam param) {
        return refundOrderAdminService.refund(param);
    }

    /// 同步退款状态
    public RefundOrderResult sync(Long id) {
        return refundOrderAdminService.sync(id);
    }
}
