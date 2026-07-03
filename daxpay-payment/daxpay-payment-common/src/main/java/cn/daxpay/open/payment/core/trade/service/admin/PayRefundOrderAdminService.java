package cn.daxpay.open.payment.core.trade.service.admin;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.payment.core.trade.convert.PayRefundOrderConvert;
import cn.daxpay.open.payment.core.trade.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.core.trade.entity.PayRefundOrder;
import cn.daxpay.open.payment.core.trade.param.PayRefundOrderQuery;
import cn.daxpay.open.payment.core.trade.param.PayRefundParam;
import cn.daxpay.open.payment.core.trade.result.PayRefundOrderResult;
import cn.daxpay.open.payment.core.trade.service.PayRefundService;
import cn.daxpay.open.payment.core.trade.service.PayRefundSyncService;
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

    /// 分页查询
    public PageResult<PayRefundOrderResult> page(PageParam pageParam, PayRefundOrderQuery query) {
        Page<PayRefundOrder> page = payRefundOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(PayRefundOrderConvert.CONVERT::toResult)
                .toList();
        return new PageResult<PayRefundOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询
    public PayRefundOrderResult findById(Long id) {
        PayRefundOrder entity = payRefundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        return PayRefundOrderConvert.CONVERT.toResult(entity);
    }

    /// 发起退款
    public PayRefundOrderResult refund(PayRefundParam param) {
        PayRefundOrder refundOrder = payRefundService.refund(param);
        return PayRefundOrderConvert.CONVERT.toResult(refundOrder);
    }

    /// 同步退款状态(传入退款单ID)
    public PayRefundOrderResult sync(Long id) {
        PayRefundOrder refundOrder = payRefundSyncService.syncById(id);
        return PayRefundOrderConvert.CONVERT.toResult(refundOrder);
    }
}
