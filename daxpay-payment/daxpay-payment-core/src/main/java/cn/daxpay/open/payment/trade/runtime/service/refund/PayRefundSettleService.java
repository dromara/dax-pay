package cn.daxpay.open.payment.trade.runtime.service.refund;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.service.plugin.PayPluginAssistService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;

/// # 退款结算服务
///
/// 资金口径采用 **发起时预占可退余额**:
/// - 建单时从 [PayTrade#getRefundableBalance] 预扣
/// - SUCCESS 仅改退款单状态, **不再扣余额**
/// - FAIL/CLOSE 回滚预占金额
///
/// 发起 / 同步 / 回调三条入口共用, 以 trade 级锁保证幂等与并发安全。
/// 锁键: `payment:refund:trade:{tradeNo}`。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundSettleService {

    /// 退款结算锁前缀(按原支付交易号)
    public static final String LOCK_PREFIX = "payment:refund:trade:";

    private final PayRefundOrderManager payRefundOrderManager;
    private final PayTradeManager payTradeManager;
    private final LockExecutor lockExecutor;
    private final PayPluginAssistService payPluginAssistService;

    /// 构建退款结算锁键
    public static String lockKey(String tradeNo) {
        return LOCK_PREFIX + tradeNo;
    }

    /// 预占可退余额(调用方须已持有 [lockKey] 对应锁)
    @Transactional(rollbackFor = Exception.class)
    public void reserveBalanceUnderLock(PayTrade trade, long amount) {
        long balance = trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance();
        if (amount > balance) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.amountExceed");
        }
        trade.setRefundableBalance(balance - amount);
        payTradeManager.updateById(trade);
    }

    /// 退款成功结算(自行加锁)。返回 true 表示本次首次进入 SUCCESS。
    public boolean settleSuccess(Long refundOrderId, OffsetDateTime finishTime, String outRefundNo) {
        PayRefundOrder boot = payRefundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        return lockExecutor.execute(
                lockKey(boot.getOrderNo()),
                () -> settleSuccessUnderLock(refundOrderId, finishTime, outRefundNo),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.processing")
        );
    }

    /// 调用方已持有 [lockKey] 对应锁时使用。
    /// SUCCESS 不改可退余额(发起时已预占); 已 SUCCESS 幂等返回 false。
    @Transactional(rollbackFor = Exception.class)
    public boolean settleSuccessUnderLock(Long refundOrderId, OffsetDateTime finishTime, String outRefundNo) {
        PayRefundOrder refundOrder = payRefundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        String oldStatus = refundOrder.getStatus();
        if (Objects.equals(oldStatus, RefundOrderStatusEnum.SUCCESS.getCode())) {
            log.info("退款成功结算幂等: refundNo={} 已是 SUCCESS", refundOrder.getRefundNo());
            return false;
        }
        // 失败/关闭等终态不可再成功
        if (Objects.equals(oldStatus, RefundOrderStatusEnum.FAIL.getCode())
                || Objects.equals(oldStatus, RefundOrderStatusEnum.CLOSE.getCode())) {
            log.warn("退款成功结算忽略: refundNo={} 已是终态 {}", refundOrder.getRefundNo(), oldStatus);
            return false;
        }

        if (finishTime != null) {
            refundOrder.setFinishTime(finishTime);
        }
        if (StrUtil.isNotBlank(outRefundNo)) {
            refundOrder.setOutRefundNo(outRefundNo);
        }
        refundOrder.setStatus(RefundOrderStatusEnum.SUCCESS.getCode());
        refundOrder.setErrorMsg(null);
        payRefundOrderManager.updateById(refundOrder);
        // 插件: 按原支付 tradeNo 回查资金单后广播退款成功
        payTradeManager.findByTradeNo(refundOrder.getOrderNo()).ifPresent(trade ->
                payPluginAssistService.refundSuccess(trade, refundOrder));
        return true;
    }

    /// 退款失败结算(自行加锁)。progress→fail 时回滚预占。
    public boolean settleFail(Long refundOrderId, OffsetDateTime finishTime, String outRefundNo, String errorMsg) {
        return settleFailOrClose(refundOrderId, false, finishTime, outRefundNo, errorMsg);
    }

    /// 退款关闭结算(自行加锁)。progress→close 时回滚预占。
    public boolean settleClose(Long refundOrderId, OffsetDateTime finishTime, String outRefundNo, String errorMsg) {
        return settleFailOrClose(refundOrderId, true, finishTime, outRefundNo, errorMsg);
    }

    /// 失败/关闭结算(自行加锁)
    public boolean settleFailOrClose(Long refundOrderId, boolean close,
                                     OffsetDateTime finishTime, String outRefundNo, String errorMsg) {
        PayRefundOrder boot = payRefundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        return lockExecutor.execute(
                lockKey(boot.getOrderNo()),
                () -> settleFailOrCloseUnderLock(refundOrderId, close, finishTime, outRefundNo, errorMsg),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.processing")
        );
    }

    /// 调用方已持有锁: progress→fail/close 并回滚预占。
    @Transactional(rollbackFor = Exception.class)
    public boolean settleFailOrCloseUnderLock(Long refundOrderId, boolean close,
                                              OffsetDateTime finishTime, String outRefundNo, String errorMsg) {
        PayRefundOrder refundOrder = payRefundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        String oldStatus = refundOrder.getStatus();
        // 已成功不可改失败
        if (Objects.equals(oldStatus, RefundOrderStatusEnum.SUCCESS.getCode())) {
            log.warn("退款失败结算忽略: refundNo={} 已是 SUCCESS", refundOrder.getRefundNo());
            return false;
        }
        String target = close ? RefundOrderStatusEnum.CLOSE.getCode() : RefundOrderStatusEnum.FAIL.getCode();
        // 已是目标终态幂等
        if (Objects.equals(oldStatus, target)
                || Objects.equals(oldStatus, RefundOrderStatusEnum.FAIL.getCode())
                || Objects.equals(oldStatus, RefundOrderStatusEnum.CLOSE.getCode())) {
            log.info("退款失败/关闭结算幂等: refundNo={} status={}", refundOrder.getRefundNo(), oldStatus);
            return false;
        }

        // progress/init → 回滚预占
        if (Objects.equals(oldStatus, RefundOrderStatusEnum.PROGRESS.getCode())
                || Objects.equals(oldStatus, RefundOrderStatusEnum.INIT.getCode())) {
            restoreBalance(refundOrder);
        }

        if (finishTime != null) {
            refundOrder.setFinishTime(finishTime);
        }
        if (StrUtil.isNotBlank(outRefundNo)) {
            refundOrder.setOutRefundNo(outRefundNo);
        }
        refundOrder.setStatus(target);
        if (errorMsg != null) {
            refundOrder.setErrorMsg(StrUtil.maxLength(errorMsg, 500));
        }
        payRefundOrderManager.updateById(refundOrder);
        return true;
    }

    /// 非终态回写(如 PROGRESS 补 outRefundNo)。不触碰可退余额。
    @Transactional(rollbackFor = Exception.class)
    public void applyProgressResult(PayRefundOrder refundOrder, OffsetDateTime finishTime,
                                    String outRefundNo, String errorMsg) {
        // 仅非终态可更新
        String status = refundOrder.getStatus();
        if (Objects.equals(status, RefundOrderStatusEnum.SUCCESS.getCode())
                || Objects.equals(status, RefundOrderStatusEnum.FAIL.getCode())
                || Objects.equals(status, RefundOrderStatusEnum.CLOSE.getCode())) {
            return;
        }
        if (finishTime != null) {
            refundOrder.setFinishTime(finishTime);
        }
        if (StrUtil.isNotBlank(outRefundNo)) {
            refundOrder.setOutRefundNo(outRefundNo);
        }
        refundOrder.setStatus(RefundOrderStatusEnum.PROGRESS.getCode());
        if (errorMsg != null) {
            refundOrder.setErrorMsg(StrUtil.maxLength(errorMsg, 500));
        } else {
            refundOrder.setErrorMsg(null);
        }
        payRefundOrderManager.updateById(refundOrder);
    }

    /// 回滚预占金额到原支付 Trade
    private void restoreBalance(PayRefundOrder refundOrder) {
        PayTrade trade = payTradeManager.findByTradeNo(refundOrder.getOrderNo())
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
        long balance = trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance();
        long amount = refundOrder.getAmount() == null ? 0 : refundOrder.getAmount();
        trade.setRefundableBalance(balance + amount);
        payTradeManager.updateById(trade);
        log.info("退款预占回滚: refundNo={}, amount={}, newBalance={}",
                refundOrder.getRefundNo(), amount, trade.getRefundableBalance());
    }
}
