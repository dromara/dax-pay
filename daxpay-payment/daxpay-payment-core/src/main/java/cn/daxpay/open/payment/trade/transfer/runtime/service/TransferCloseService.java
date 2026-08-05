package cn.daxpay.open.payment.trade.transfer.runtime.service;

import cn.daxpay.open.payment.strategy.transfer.AbsTransferStrategy;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyContext;
import cn.daxpay.open.payment.strategy.transfer.TransferStrategyFactory;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 转账关闭服务
///
/// 管理端手动关闭转账单。仅通道支持场景有效（支付宝可撤销，微信不支持），
/// 通道支持性由策略 [AbsTransferStrategy#doClose] 判定。
/// 容器读写收敛在 [TransferAssistService]，本服务只面向凭证与策略上下文。
/// 锁键 `payment:transfer-trade:{id}` 与同步/回调路径互斥。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferCloseService {

    private final TransferAssistService assistService;
    private final TransferTradeManager transferTradeManager;
    private final LockExecutor lockExecutor;

    /// 关闭转账（管理端手动）
    ///
    /// @param channel 通道编码
    /// @param id      转账单主键
    public void close(String channel, Long id) {
        TransferTrade trade = assistService.findTradeByContainer(channel, id)
                .orElseThrow(() -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.notFound"));
        lockExecutor.run(TransferAssistService.tradeLockKey(trade.getId()), () -> {
            // 锁内二次读: 仅处理中可关闭
            TransferTrade latestTrade = transferTradeManager.findById(trade.getId()).orElse(null);
            if (latestTrade == null
                    || !Objects.equals(latestTrade.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
                log.info("转账关闭幂等: 凭证 {} 非处理中, 跳过", trade.getTradeNo());
                return;
            }
            // 装载容器并装配策略上下文
            TransferStrategyContext context = assistService.loadContext(channel, latestTrade.getContainerId()).orElse(null);
            if (context == null || !Objects.equals(context.getStatus(), PayFundStatusEnum.PROCESSING.getCode())) {
                log.info("转账关闭幂等: 容器 {} 非处理中, 跳过", latestTrade.getTradeNo());
                return;
            }
            // 通道支持性校验(不支持直接抛错, 不改变状态)
            AbsTransferStrategy strategy = TransferStrategyFactory.create(channel);
            strategy.doClose(context);
            assistService.close(channel, latestTrade, null);
        }, () -> new BizInfoException(CommonCode.FAIL_CODE, "pay.error.transfer.closeNotProcessing"));
    }
}
