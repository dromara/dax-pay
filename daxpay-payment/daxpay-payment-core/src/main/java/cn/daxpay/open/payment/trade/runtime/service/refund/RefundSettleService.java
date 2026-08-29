package cn.daxpay.open.payment.trade.runtime.service.refund;

import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.dao.RefundOrderManager;
import cn.daxpay.open.payment.trade.order.entity.RefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.notice.service.TradeNoticeBridge;
import cn.daxpay.open.payment.trade.flow.service.FundFlowService;
import cn.daxpay.open.payment.trade.runtime.service.plugin.PayPluginAssistService;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.enums.pay.notice.NoticeEventEnum;
import cn.daxpay.open.platform.core.exception.BizErrorException;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

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
public class RefundSettleService {

    /// 退款结算锁前缀(按原支付交易号)
    public static final String LOCK_PREFIX = "payment:refund:trade:";

    private final RefundOrderManager refundOrderManager;
    private final PayTradeManager payTradeManager;
    private final LockExecutor lockExecutor;
    private final PayPluginAssistService payPluginAssistService;
    private final TradeNoticeBridge tradeNoticeBridge;
    private final FundFlowService fundFlowService;

    /// 自身注入: 使 UnderLock 的 @Transactional 方法经代理调用生效(锁外层 + 事务内层),
    /// 消除"自行加锁路径下 this 调用导致事务失效"的隐患(同步路径无外层事务时尤为关键)
    @Lazy
    private final RefundSettleService self;

    /// 构建退款结算锁键
    public static String lockKey(String tradeNo) {
        return LOCK_PREFIX + tradeNo;
    }

    /// 预占可退余额(调用方须已持有 [lockKey] 对应锁)
    @Transactional(rollbackFor = Exception.class)
    public void reserveBalanceUnderLock(PayTrade trade, long amount) {
        // 退款金额必须大于零（防止零值/负值放行导致 refundableBalance 反增）
        if (amount <= 0) {
            // 退款: 退款金额必须大于零
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.amountInvalid");
        }
        long balance = trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance();
        if (amount > balance) {
            // 退款: 退款金额不能大于支付金额
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.amountExceed");
        }
        trade.setRefundableBalance(balance - amount);
        payTradeManager.updateById(trade);
    }

    /// 退款成功结算(自行加锁)。返回 true 表示本次首次进入 SUCCESS。
    public boolean settleSuccess(Long refundOrderId, OffsetDateTime finishTime,
                                 String outRefundNo, String relationOrderNo) {
        RefundOrder boot = refundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        return lockExecutor.execute(
                lockKey(boot.getTradeNo()),
                () -> self.settleSuccessUnderLock(refundOrderId, finishTime, outRefundNo, relationOrderNo),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.processing")
        );
    }

    /// 调用方已持有 [lockKey] 对应锁时使用。
    /// SUCCESS 不改可退余额(发起时已预占); 已 SUCCESS 幂等返回 false。
    @Transactional(rollbackFor = Exception.class)
    public boolean settleSuccessUnderLock(Long refundOrderId, OffsetDateTime finishTime,
                                          String outRefundNo, String relationOrderNo) {
        RefundOrder refundOrder = refundOrderManager.findById(refundOrderId)
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

        applyChannelRefs(refundOrder, finishTime, outRefundNo, relationOrderNo);
        refundOrder.setStatus(RefundOrderStatusEnum.SUCCESS.getCode());
        refundOrder.setErrorMsg(null);
        // CAS: 仅 PROGRESS 可转 SUCCESS（锁内防御性兜底，与支付侧对称）
        boolean updated = refundOrderManager.casUpdateStatus(
                refundOrder, Set.of(RefundOrderStatusEnum.PROGRESS.getCode()));
        if (!updated) {
            log.info("退款成功结算CAS竞争失败: refundNo={} 状态已被其他线程改变",
                    refundOrder.getRefundNo());
            return false;
        }
        // 资金流水(退款支出, 幂等)
        fundFlowService.saveRefundFlow(refundOrder);
        // 商户出站通知(系统协议)
        tradeNoticeBridge.dispatchRefund(refundOrder, NoticeEventEnum.REFUND_SUCCESS);
        // 插件: 按原支付 tradeNo 回查资金单后广播退款成功
        payTradeManager.findByTradeNo(refundOrder.getTradeNo()).ifPresent(trade ->
                payPluginAssistService.refundSuccess(trade, refundOrder));
        return true;
    }

    /// 退款失败结算(自行加锁)。progress→fail 时回滚预占。
    public boolean settleFail(Long refundOrderId, OffsetDateTime finishTime,
                              String outRefundNo, String relationOrderNo, String errorMsg) {
        return settleFailOrClose(refundOrderId, false, finishTime, outRefundNo, relationOrderNo, errorMsg);
    }

    /// 退款关闭结算(自行加锁)。progress→close 时回滚预占。
    public boolean settleClose(Long refundOrderId, OffsetDateTime finishTime,
                               String outRefundNo, String relationOrderNo, String errorMsg) {
        return settleFailOrClose(refundOrderId, true, finishTime, outRefundNo, relationOrderNo, errorMsg);
    }

    /// 失败/关闭结算(自行加锁)
    public boolean settleFailOrClose(Long refundOrderId, boolean close,
                                     OffsetDateTime finishTime, String outRefundNo,
                                     String relationOrderNo, String errorMsg) {
        RefundOrder boot = refundOrderManager.findById(refundOrderId)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.orderNotFound"));
        return lockExecutor.execute(
                lockKey(boot.getTradeNo()),
                () -> self.settleFailOrCloseUnderLock(refundOrderId, close, finishTime, outRefundNo, relationOrderNo, errorMsg),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.processing")
        );
    }

    /// 调用方已持有锁: progress→fail/close 并回滚预占。
    @Transactional(rollbackFor = Exception.class)
    public boolean settleFailOrCloseUnderLock(Long refundOrderId, boolean close,
                                              OffsetDateTime finishTime, String outRefundNo,
                                              String relationOrderNo, String errorMsg) {
        RefundOrder refundOrder = refundOrderManager.findById(refundOrderId)
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

        // 先 CAS 改退款单状态, 成功后才回滚预占余额;
        // 若先回滚余额再 CAS, 一旦 CAS 失败(CAS 前事务未回滚)会导致"余额已回滚但状态仍 PROGRESS",
        // 定时同步再次进入将重复回滚余额造成超发
        applyChannelRefs(refundOrder, finishTime, outRefundNo, relationOrderNo);
        refundOrder.setStatus(target);
        if (errorMsg != null) {
            refundOrder.setErrorMsg(StrUtil.maxLength(errorMsg, 500));
        }
        // CAS: 仅 PROGRESS 可转 FAIL/CLOSE（锁内防御性兜底）
        boolean updated = refundOrderManager.casUpdateStatus(
                refundOrder, Set.of(RefundOrderStatusEnum.PROGRESS.getCode()));
        if (!updated) {
            // CAS 失败: 状态已被其他线程改为终态, 抛异常回滚事务(含 applyChannelRefs 的内存改动),
            // restoreBalance 尚未执行, 无超发风险; 不抛异常会导致事务正常提交留下不一致
            throw new BizErrorException(CommonErrorCode.SYSTEM_ERROR,
                    "pay.error.refund.casConflict", refundOrder.getRefundNo());
        }
        // CAS 成功后才回滚预占金额(progress → 终态)
        if (Objects.equals(oldStatus, RefundOrderStatusEnum.PROGRESS.getCode())) {
            restoreBalance(refundOrder);
        }
        // 商户出站通知: 关闭终态发 refund.close；失败终态发 refund.fail
        if (close) {
            tradeNoticeBridge.dispatchRefund(refundOrder, NoticeEventEnum.REFUND_CLOSE);
        } else {
            tradeNoticeBridge.dispatchRefund(refundOrder, NoticeEventEnum.REFUND_FAIL);
        }
        // 插件: 失败/关闭广播
        payTradeManager.findByTradeNo(refundOrder.getTradeNo()).ifPresent(trade ->
                payPluginAssistService.refundClose(trade, refundOrder));
        return true;
    }

    /// 非终态回写(如 PROGRESS 补 outRefundNo / relationOrderNo)。不触碰可退余额。
    @Transactional(rollbackFor = Exception.class)
    public void applyProgressResult(RefundOrder refundOrder, OffsetDateTime finishTime,
                                    String outRefundNo, String relationOrderNo, String errorMsg) {
        // 仅非终态可更新
        String status = refundOrder.getStatus();
        if (Objects.equals(status, RefundOrderStatusEnum.SUCCESS.getCode())
                || Objects.equals(status, RefundOrderStatusEnum.FAIL.getCode())
                || Objects.equals(status, RefundOrderStatusEnum.CLOSE.getCode())) {
            return;
        }
        applyChannelRefs(refundOrder, finishTime, outRefundNo, relationOrderNo);
        refundOrder.setStatus(RefundOrderStatusEnum.PROGRESS.getCode());
        if (errorMsg != null) {
            refundOrder.setErrorMsg(StrUtil.maxLength(errorMsg, 500));
        } else {
            refundOrder.setErrorMsg(null);
        }
        // CAS: 仅非终态(PROGRESS)可回写（防御性兜底）
        refundOrderManager.casUpdateStatus(
                refundOrder, Set.of(RefundOrderStatusEnum.PROGRESS.getCode()));
    }

    /// 回写通道关联号/完成时间
    private void applyChannelRefs(RefundOrder refundOrder, OffsetDateTime finishTime,
                                  String outRefundNo, String relationOrderNo) {
        if (finishTime != null) {
            refundOrder.setFinishTime(finishTime);
        }
        if (StrUtil.isNotBlank(outRefundNo)) {
            refundOrder.setOutRefundNo(outRefundNo);
        }
        if (StrUtil.isNotBlank(relationOrderNo)) {
            refundOrder.setRelationOrderNo(relationOrderNo);
        }
    }

    /// 回滚预占金额到原支付 Trade
    private void restoreBalance(RefundOrder refundOrder) {
        PayTrade trade = payTradeManager.findByTradeNo(refundOrder.getTradeNo())
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
        long balance = trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance();
        long amount = refundOrder.getAmount() == null ? 0 : refundOrder.getAmount();
        trade.setRefundableBalance(balance + amount);
        payTradeManager.updateById(trade);
        log.info("退款预占回滚: refundNo={}, amount={}, newBalance={}",
                refundOrder.getRefundNo(), amount, trade.getRefundableBalance());
    }
}
