package cn.daxpay.open.payment.admin.service.trade;

import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.admin.convert.trade.PayRefundOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.trade.order.param.PayRefundOrderQuery;
import cn.daxpay.open.payment.trade.runtime.param.PayRefundParam;
import cn.daxpay.open.payment.trade.order.result.PayRefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.service.refund.PayRefundService;
import cn.daxpay.open.payment.trade.runtime.service.refund.PayRefundSyncService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 退款订单管理服务(管理端)
///
/// 提供退款订单的分页/详情查询, 以及发起退款、退款状态同步操作。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundOrderAdminService {

    private final PayRefundOrderManager payRefundOrderManager;
    private final PayRefundService payRefundService;
    private final PayRefundSyncService payRefundSyncService;
    private final TransService transService;

    /// 分页查询
    public PageResult<PayRefundOrderResult> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> page = payRefundOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(PayRefundOrderConvert.CONVERT::toResult)
                .toList();
        PageResult<PayRefundOrderResult> pageResult = new PageResult<PayRefundOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        // 翻译商户名称(mchNo -> mchName)
        transService.translate(pageResult);
        return pageResult;
    }

    /// 详情查询
    public PayRefundOrderResult findById(Long id) {
        PayRefundOrder entity = payRefundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        PayRefundOrderResult result = PayRefundOrderConvert.CONVERT.toResult(entity);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 发起退款
    public PayRefundOrderResult refund(PayRefundParam param) {
        PayRefundOrder refundOrder = payRefundService.refund(param);
        PayRefundOrderResult result = PayRefundOrderConvert.CONVERT.toResult(refundOrder);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }

    /// 同步退款状态(传入退款单ID)
    public PayRefundOrderResult sync(Long id) {
        PayRefundOrder refundOrder = payRefundSyncService.syncById(id);
        PayRefundOrderResult result = PayRefundOrderConvert.CONVERT.toResult(refundOrder);
        // 翻译商户名称
        transService.translate(result);
        return result;
    }
}
