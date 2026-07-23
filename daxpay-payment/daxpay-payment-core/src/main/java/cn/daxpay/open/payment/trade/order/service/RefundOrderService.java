package cn.daxpay.open.payment.trade.order.service;

import cn.daxpay.open.platform.common.translate.service.TransService;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.payment.trade.order.convert.RefundOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundService;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 退款订单共享服务（管理端查询/编排）
///
/// 运营端 / 商户端共用。与 runtime [RefundService]（退款引擎）职责不同。
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOrderService {

    private final RefundOrderManager refundOrderManager;
    private final RefundService refundService;
    private final RefundSyncService refundSyncService;
    private final TransService transService;
    private final ClientCodeService clientCodeService;

    /// 分页查询
    public PageResult<RefundOrderResult> page(PageParam pageParam, RefundOrderQuery query) {
        sanitizeQuery(query);
        Page<RefundOrder> page = refundOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(RefundOrderConvert.CONVERT::toResult)
                .toList();
        PageResult<RefundOrderResult> pageResult = new PageResult<RefundOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
        translateIfAdmin(pageResult);
        return pageResult;
    }

    /// 详情查询
    public RefundOrderResult findById(Long id) {
        RefundOrder entity = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(entity);
        translateIfAdmin(result);
        return result;
    }

    /// 发起退款
    public RefundOrderResult refund(RefundParam param) {
        RefundOrder refundOrder = refundService.refund(param);
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(refundOrder);
        translateIfAdmin(result);
        return result;
    }

    /// 同步退款状态(传入退款单ID)
    public RefundOrderResult sync(Long id) {
        RefundOrder refundOrder = refundSyncService.syncById(id);
        RefundOrderResult result = RefundOrderConvert.CONVERT.toResult(refundOrder);
        translateIfAdmin(result);
        return result;
    }

    private void sanitizeQuery(RefundOrderQuery query) {
        if (query == null) {
            return;
        }
        if (ClientEnum.MERCHANT.getCode().equals(clientCodeService.getClientCode())) {
            query.setMchNo(null);
        }
    }

    private void translateIfAdmin(Object target) {
        if (ClientEnum.ADMIN.getCode().equals(clientCodeService.getClientCode())) {
            // 翻译商户名称(mchNo -> mchName)
            transService.translate(target);
        }
    }
}
