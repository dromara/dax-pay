package cn.daxpay.open.payment.merchant.service.trade;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.convert.RefundOrderConvert;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.param.RefundOrderQuery;
import cn.daxpay.open.payment.trade.order.result.RefundOrderResult;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.runtime.param.RefundParam;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundService;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSettleService;
import cn.daxpay.open.payment.trade.runtime.service.refund.RefundSyncService;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

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
    private final RefundSettleService refundSettleService;

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

    /// 手动关闭异常退款单
    ///
    /// 仅允许处理 PROGRESS 且创建超过 7 天的退款单(定时同步任务自然淘汰后的滞留单,
    /// 余额永久预占无法自动恢复)。关闭前必须查通道确认未成功退款, 避免误关已退款导致双重支出。
    /// 行级隔离由 TenantLine 兜底,商户只能操作自己的退款单。
    public RefundOrderResult manualClose(Long id) {
        RefundOrder refundOrder = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        // 校验: 仅 PROGRESS 可手动关闭
        if (!Objects.equals(RefundOrderStatusEnum.PROGRESS.getCode(), refundOrder.getStatus())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.manualCloseStatus");
        }
        // 校验: 创建须超过 7 天(与定时同步淘汰窗口对齐)
        if (refundOrder.getCreateTime() == null
                || refundOrder.getCreateTime().isAfter(OffsetDateTime.now(ZoneOffset.UTC).minusDays(7))) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.manualCloseTooEarly");
        }
        // 关闭前查通道确认未成功退款, 避免误关已退款导致平台双重支出
        RefundResultBo channelResult = refundSyncService.queryChannel(refundOrder);
        if (!channelResult.isSyncSuccess() || channelResult.getStatus() == null) {
            // 通道查单失败, 无法确认状态, 拒绝关闭(可稍后重试)
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.closeChannelQueryFailed");
        }
        if (Objects.equals(channelResult.getStatus(), RefundOrderStatusEnum.SUCCESS)) {
            // 通道实际已退款成功: 改为结算成功(纠正)而非关闭
            refundSettleService.settleSuccess(
                    refundOrder.getId(), channelResult.getFinishTime(),
                    channelResult.getOutRefundNo(), channelResult.getRelationOrderNo());
            // 退款: 通道确认该退款已成功, 已自动纠正为成功, 无需关闭
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.alreadySuccessOnClose");
        }
        // 通道确认非成功(PROGRESS/FAIL/CLOSE), 可安全关闭
        // 结算关闭(自行加 trade 级锁, 回滚预占余额)
        refundSettleService.settleClose(
                refundOrder.getId(), OffsetDateTime.now(ZoneOffset.UTC),
                null, null, "商户手动关闭异常退款单");
        RefundOrder updated = refundOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.refund.orderNotFound"));
        return RefundOrderConvert.CONVERT.toResult(updated);
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
