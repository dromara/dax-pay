package cn.daxpay.open.plugin.easypay.service.order;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.plugin.easypay.dao.EasyPayRefundOrderManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayRefundOrder;
import cn.daxpay.open.plugin.easypay.param.order.EasyPayRefundOrderQuery;
import cn.daxpay.open.plugin.easypay.result.order.EasyPayRefundOrderResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 易支付协议退款订单查询服务(商户端)
///
/// 承接商户端的易支付退款订单管理入口：分页、详情、同步。
/// 强制按 [PaymentContext#getMchNo] 过滤；行级隔离另由 TenantLine 兜底。
/// 同步直达内核 [RefundSyncService] 并回写易支付退款单状态（等价原共享查询服务的透传行为）。
/// 生命周期钩子(双写/成功回写) 见 [EasyPayRefundOrderService], 与本类解耦以避免循环依赖:
/// 钩子链位于 [cn.daxpay.open.payment.trade.runtime.service.plugin.PayPluginAssistService] 的下游,
/// 若本类同时持有 RefundOrderService 用于同步透传, 会经 RefundOrderService -> RefundService
/// -> RefundSettleService -> PayPluginAssistService -> 插件链 -> 本类 形成构造期循环依赖。
@Slf4j
@Service
@RequiredArgsConstructor
public class EasyPayRefundOrderMchQueryService {

    private final PaymentContext paymentContext;
    private final EasyPayRefundOrderManager easyPayRefundOrderManager;
    private final RefundSyncService refundSyncService;

    /// 分页查询(强制当前商户)
    public PageResult<EasyPayRefundOrderResult> page(PageParam pageParam, EasyPayRefundOrderQuery query) {
        forceMchNo(query);
        Page<EasyPayRefundOrder> page = easyPayRefundOrderManager.page(pageParam, query);
        var records = page.getRecords().stream()
                .map(EasyPayRefundOrder::toResult)
                .toList();
        return new PageResult<EasyPayRefundOrderResult>()
                .setRecords(records)
                .setTotal(page.getTotal())
                .setSize(page.getSize())
                .setCurrent(page.getCurrent());
    }

    /// 详情查询
    public EasyPayRefundOrderResult findById(Long id) {
        EasyPayRefundOrder entity = easyPayRefundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        return entity.toResult();
    }

    /// 同步退款状态(直达内核退款同步, 并回写易支付退款单状态)
    public EasyPayRefundOrderResult sync(Long id) {
        EasyPayRefundOrder entity = easyPayRefundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        RefundOrder refundOrder = refundSyncService.syncById(entity.getRefundId());
        entity.setStatus(mapStatus(refundOrder.getStatus()));
        if (Objects.equals(entity.getStatus(), 1) && entity.getEndTime() == null) {
            entity.setEndTime(OffsetDateTime.now(ZoneOffset.UTC));
        }
        easyPayRefundOrderManager.updateById(entity);
        return entity.toResult();
    }

    /// 内核退款状态码 → 易支付协议状态(1=成功, 0=其它)
    private int mapStatus(String kernelStatus) {
        return Objects.equals(kernelStatus, RefundOrderStatusEnum.SUCCESS.getCode()) ? 1 : 0;
    }

    /// 解析并强制写入当前商户号
    private void forceMchNo(EasyPayRefundOrderQuery query) {
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
