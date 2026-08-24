package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferSyncService;
import cn.daxpay.open.payment.unipay.param.trade.transfer.TransferSyncParam;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferSyncResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 转账订单同步服务(对外)
///
/// 对外统一入口: 定位转账凭证后委托 [TransferSyncService] 查询通道终态回写。
/// 核心同步入参为 容器通道+容器主键, 非处理中单在锁内幂等跳过(重复同步无害)。
/// 核心同步无返回值, 由本服务前后回查凭证比较状态得出 adjust, 让商户感知本次同步是否触发了状态调整,
/// 与退款同步 [RefundOrderSyncService] 的 adjust 语义对齐。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderSyncService {

    private final TransferOrderLocateService transferOrderLocateService;
    private final TransferTradeManager transferTradeManager;
    private final TransferSyncService transferSyncService;

    /// 转账同步
    public TransferSyncResult sync(TransferSyncParam param) {
        TransferTrade trade = transferOrderLocateService.locate(
                param.getMchNo(), param.getTransferNo(), param.getChannel(), param.getBizTransferNo());
        // 记录同步前状态, 同步后比较得出是否调整
        String statusBefore = trade.getStatus();
        // 核心同步按 容器通道+容器主键 定位
        transferSyncService.sync(trade.getContainerChannel(), trade.getContainerId());
        TransferTrade latest = transferTradeManager.findByTradeNo(trade.getTradeNo())
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
        boolean adjust = !Objects.equals(statusBefore, latest.getStatus());

        return new TransferSyncResult()
                .setOrderStatus(latest.getStatus())
                .setAdjust(adjust);
    }
}
