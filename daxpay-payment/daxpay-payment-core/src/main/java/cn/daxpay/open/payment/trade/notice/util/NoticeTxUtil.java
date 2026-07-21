package cn.daxpay.open.payment.trade.notice.util;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/// # 事务提交后执行工具
///
public final class NoticeTxUtil {

    private NoticeTxUtil() {
    }

    /// 有活跃事务则 afterCommit 执行，否则立即执行
    public static void afterCommit(Runnable runnable) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }
}
