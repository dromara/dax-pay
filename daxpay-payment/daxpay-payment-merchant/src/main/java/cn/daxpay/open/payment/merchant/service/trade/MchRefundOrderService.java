package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.order.convert.RefundOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundService;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 退款订单(商户端)
///
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
@Service
@RequiredArgsConstructor
public class MchRefundOrderService {

    private final PaymentContext paymentContext;
    private final RefundOrderManager refundOrderManager;
    private final RefundService refundService;
    private final RefundSyncService refundSyncService;

    /// 分页查询(强制当前商户)
    public PageResult<RefundOrderResult> page(PageParam pageParam, RefundOrderQuery query) {
        forceMchNo(query);
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(RefundOrderConvert.CONVERT::toResult)
                .toList();
        return new PageResult<RefundOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询
    public RefundOrderResult findById(Long id) {
        RefundOrder entity = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        return RefundOrderConvert.CONVERT.toResult(entity);
    }

    /// 发起退款
    public RefundOrderResult refund(RefundParam param) {
        RefundOrder refundOrder = refundService.refund(param);
        return RefundOrderConvert.CONVERT.toResult(refundOrder);
    }

    /// 同步退款状态(传入退款单ID)
    public RefundOrderResult sync(Long id) {
        RefundOrder refundOrder = refundSyncService.syncById(id);
        return RefundOrderConvert.CONVERT.toResult(refundOrder);
    }

    /// 解析并强制写入当前商户号
    private void forceMchNo(RefundOrderQuery query) {
        String mchNo = requireMchNo();
        if (query == null) {
            return;
        }
        query.setMchNo(mchNo);
    }

    private String requireMchNo() {
        String mchNo = paymentContext.getMchNo();
        if (mchNo == null || mchNo.isBlank()) {
            // 商户: 数据错误未发现商户号
            throw new BizInfoException(CommonCode.FAIL_CODE, "error.payment.merchant.dataErrorNoMchNo");
        }
        return mchNo;
    }
}
