package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.payment.trade.order.convert.RefundOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundService;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 退款订单管理服务
///
/// 运营端专属。跨商户查询，按 mchNo 翻译商户名称。
@Service
@RequiredArgsConstructor
public class RefundOrderAdminService {

    private final RefundOrderManager refundOrderManager;
    private final RefundService refundService;
    private final RefundSyncService refundSyncService;
    private final TransService transService;

    /// 分页查询
    public PageResult<RefundOrderResult> page(PageParam pageParam, RefundOrderQuery query) {
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(RefundOrderConvert.CONVERT::toResult)
                .toList();
        PageResult<RefundOrderResult> pageResult = new PageResult<RefundOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询
    public RefundOrderResult findById(Long id) {
        RefundOrder entity = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(entity);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 发起退款
    public RefundOrderResult refund(RefundParam param) {
        RefundOrder refundOrder = refundService.refund(param);
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(refundOrder);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 同步退款状态(传入退款单ID)
    public RefundOrderResult sync(Long id) {
        RefundOrder refundOrder = refundSyncService.syncById(id);
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(refundOrder);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }
}
