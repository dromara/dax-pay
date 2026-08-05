package cn.daxpay.open.payment.trade.transfer.runtime.service;

import cn.daxpay.open.payment.trade.record.service.PayCallbackRecordService;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.enums.pay.notice.CallbackStatusEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/// # 转账回调处理
///
/// 与 [cn.daxpay.open.payment.trade.runtime.service.callback.PayCallbackService] 对称:
/// 转账回调用 transferNo / outTransferNo 反查公共资金凭证,
/// 锁键 `payment:transfer-trade:{id}` 与同步/关闭路径互斥。
/// 回调记录统一委托 [PayCallbackRecordService#saveTransfer] 落库(只审计不重放)。
///
/// 锁包事务模式: [transferCallback] 持锁(无事务) → [doTransferCallback] 走代理(@Transactional)。
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferCallbackService {

    private final TransferAssistService assistService;
    private final TransferTradeManager transferTradeManager;
    private final PayCallbackRecordService payCallbackRecordService;
    private final LockExecutor lockExecutor;

    /// 自注入: 保证 [doTransferCallback] 走 Spring 事务代理
    @Lazy
    private final TransferCallbackService self;

    /// 转账统一回调处理（锁外层, 无事务）
    ///
    /// @param channelMchNo 通道商户号(回调 path 入站身份)
    /// @param channel      通道编码
    /// @param data         回调数据(反查字段: tradeNo=transferNo / outTradeNo=outTransferNo)
    public void transferCallback(String channelMchNo, String channel, CallbackData data) {
        // 锁外层: 反查凭证获取锁键维度
        TransferTrade trade = this.resolveTrade(data);
        if (trade == null) {
            data.setCallbackStatus(CallbackStatusEnum.NOT_FOUND)
                    .setCallbackErrorMsg("转账单不存在,记录回调记录");
            log.warn("转账回调: 凭证不存在 channel={} tradeNo={} outTradeNo={}",
                    channel, data.getTradeNo(), data.getOutTradeNo());
            payCallbackRecordService.saveTransfer(channelMchNo, data);
            return;
        }
        // 统一锁键: 与同步/关闭路径互斥
        Long tradeId = trade.getId();
        boolean acquired = lockExecutor.tryRun(TransferAssistService.tradeLockKey(tradeId),
                () -> self.doTransferCallback(channelMchNo, channel, data, tradeId));
        if (!acquired) {
            data.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("回调正在处理中，忽略本次回调请求");
            log.warn("转账回调: transferNo={} 回调正在处理中，忽略本次回调请求", data.getTradeNo());
        }
    }

    /// 回调核心处理（锁内层, 事务边界）
    @Transactional(rollbackFor = Exception.class)
    public void doTransferCallback(String channelMchNo, String channel, CallbackData data, Long tradeId) {
        // 持锁后二次读取最新状态
        TransferTrade trade = transferTradeManager.findById(tradeId).orElse(null);
        if (trade == null) {
            data.setCallbackStatus(CallbackStatusEnum.NOT_FOUND)
                    .setCallbackErrorMsg("转账单不存在,记录回调记录");
            payCallbackRecordService.saveTransfer(channelMchNo, data);
            return;
        }
        // 终态守卫: 已 SUCCESS/CLOSE 幂等忽略; 非 PROCESSING 不可再流转
        String oldStatus = trade.getStatus();
        if (Objects.equals(oldStatus, "success") || Objects.equals(oldStatus, "close")) {
            data.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("转账单状态已处理,记录回调记录");
            log.info("转账回调: 凭证 {} 已处于终态 {}, 忽略", trade.getTradeNo(), oldStatus);
            payCallbackRecordService.saveTransfer(channelMchNo, data);
            return;
        }
        if (!Objects.equals(oldStatus, "processing")) {
            data.setCallbackStatus(CallbackStatusEnum.IGNORE)
                    .setCallbackErrorMsg("转账单状态非法,记录回调记录");
            log.warn("转账回调: 凭证 {} 状态为 {} 非处理中, 忽略", trade.getTradeNo(), oldStatus);
            payCallbackRecordService.saveTransfer(channelMchNo, data);
            return;
        }
        // 按回调状态流转(成功/关闭/失败均双表 CAS + 通知, 容器读写由 Assist 内部完成)
        if (Objects.equals(CallbackStatusEnum.SUCCESS.getCode(), data.getTradeStatus())) {
            assistService.success(channel, trade,
                    StrUtil.blankToDefault(data.getOutTradeNo(), trade.getOutTransferNo()),
                    data.getFinishTime(), null, null);
        } else if (Objects.equals(CallbackStatusEnum.CLOSE.getCode(), data.getTradeStatus())) {
            assistService.close(channel, trade, data.getTradeErrorMsg());
        } else {
            assistService.fail(channel, trade, data.getTradeErrorMsg());
        }
        // 落回调记录(只审计不重放, 新开事务不受业务回滚影响)
        payCallbackRecordService.saveTransfer(channelMchNo, data);
    }

    /// 反查凭证: transferNo → outTransferNo
    private TransferTrade resolveTrade(CallbackData data) {
        if (StrUtil.isNotBlank(data.getTradeNo())) {
            var byTradeNo = transferTradeManager.findByTradeNo(data.getTradeNo());
            if (byTradeNo.isPresent()) {
                return byTradeNo.get();
            }
        }
        // 容错: 部分通道仅回传其内部转账号
        if (StrUtil.isNotBlank(data.getOutTradeNo())) {
            return transferTradeManager.findByOutTransferNo(data.getOutTradeNo()).orElse(null);
        }
        return null;
    }
}

