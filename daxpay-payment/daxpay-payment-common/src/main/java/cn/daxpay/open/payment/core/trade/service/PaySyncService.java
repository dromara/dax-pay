package cn.daxpay.open.payment.core.trade.service;

import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.exception.RepetitiveOperationException;
import cn.daxpay.open.platform.core.exception.system.SystemUnknownErrorException;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.core.trade.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.record.entity.PaySyncRecord;
import cn.daxpay.open.payment.core.trade.record.service.PaySyncRecordService;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPaySyncParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

/// # 支付同步服务
///
/// 通道策略层返回同步结果, 本服务负责本地资金凭证(PayTrade)与通道状态对齐,
/// 含: 状态比对、超时主动关网关、成功/失败/关闭调整, 以及同步流水记录持久化
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final PaySyncRecordService paySyncRecordService;
    private final LockTemplate lockTemplate;

    /// 支付同步
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public NormalPaySyncResult sync(NormalPaySyncParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())
                && Objects.isNull(param.getOutOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayTrade trade = null;
        if (StrUtil.isNotBlank(param.getOrderNo())) {
            trade = payTradeManager.findByTradeNo(param.getOrderNo()).orElse(null);
        }
        if (Objects.isNull(trade) && Objects.nonNull(param.getBizOrderNo())) {
            NormalPayOrder normalOrder = payNormalOrderManager.findByBizOrderNo(
                    param.getBizOrderNo()).orElse(null);
            if (Objects.nonNull(normalOrder)) {
                trade = payTradeManager.findByContainerId(normalOrder.getId())
                        .orElse(null);
            }
        }
        if (Objects.isNull(trade) && StrUtil.isNotBlank(param.getOutOrderNo())) {
            trade = payTradeManager.findByOutOrderNo(param.getOutOrderNo())
                    .orElse(null);
        }
        if (Objects.isNull(trade)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
        }
        return this.syncPayOrder(trade);
    }

    /// 同步支付状态
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public NormalPaySyncResult syncPayOrder(PayTrade trade) {
        if (Objects.equals(trade.getStatus(), PayFundStatusEnum.INIT.getCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.syncNotStarted");
        }
        LockInfo lock = lockTemplate.lock("sync:pay:" + trade.getId(), 10000, 200);
        if (Objects.isNull(lock)) {
            throw new RepetitiveOperationException();
        }
        try {
            NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElse(null);
            var context = new PayStrategyContext()
                    .setContainer(normalOrder)
                    .setTrade(trade);
            var syncStrategy = PaymentStrategyFactory.createByProduct(
                    trade.getProduct(), AbsSyncPayOrderStrategy.class);
            PaySyncResultBo syncResult = syncStrategy.doSync(context);
            if (!Objects.equals(syncResult.getOutOrderNo(), trade.getOutOrderNo())) {
                trade.setOutOrderNo(syncResult.getOutOrderNo());
                payTradeManager.updateById(trade);
            }
            boolean statusSync = this.checkAndAdjust(syncResult, trade);
            if (!statusSync) {
                try {
                    this.adjustHandler(syncResult, trade, normalOrder);
                } catch (PayFailureException e) {
                    syncResult.setSyncSuccess(false).setSyncErrorMsg(e.getMessage());
                }
            }
            // 保存同步流水记录(成功/失败均记录)
            this.saveRecord(trade, normalOrder, syncResult, !statusSync);
            return new NormalPaySyncResult()
                    .setOrderStatus(trade.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 判断本地与通道状态是否一致, 并在本地支付中且超时时标记需要远程关网关
    private boolean checkAndAdjust(PaySyncResultBo syncResult, PayTrade trade) {
        var payStatus = Optional.ofNullable(syncResult.getPayStatus())
                .orElse(PayFundStatusEnum.PROCESSING);
        String orderStatus = trade.getStatus();
        // 本地已失败, 直接返回需要调整
        if (orderStatus.equals(PayFundStatusEnum.FAIL.getCode())) {
            return false;
        }
        if (orderStatus.equals(PayFundStatusEnum.PROCESSING.getCode())) {
            // 通道也是处理中, 进一步判断是否超时
            if (Objects.equals(PayFundStatusEnum.PROCESSING, payStatus)) {
                if (DateTimeUtil.le(trade.getExpiredTime(), OffsetDateTime.now(ZoneOffset.UTC))) {
                    // 本地超时且通道订单仍存活, 标记需要主动关闭网关交易
                    syncResult.setPayStatus(PayFundStatusEnum.CLOSE);
                    syncResult.setRemoteClose(true);
                    return false;
                }
                return true;
            }
        } else {
            // 本地非处理中(已成功/已关闭/已撤销), 视为一致
            return true;
        }
        return false;
    }

    /// 根据同步结果调整资金凭证状态
    private void adjustHandler(PaySyncResultBo syncResult, PayTrade trade, NormalPayOrder normalOrder) {
        var payStatus = syncResult.getPayStatus();
        if (Objects.isNull(payStatus)) {
            return;
        }
        switch (payStatus) {
            case PROCESSING -> {}
            case SUCCESS -> this.success(trade, normalOrder, syncResult);
            case CLOSE -> {
                // 通道已关闭 → 本地关闭; 超时(remoteClose) → 主动调用通道关单后本地关闭
                if (syncResult.isRemoteClose()) {
                    this.closeRemote(trade, normalOrder);
                } else {
                    payUniHandleService.payClose(trade, normalOrder, false);
                }
            }
            case FAIL -> payUniHandleService.payFail(trade, normalOrder, syncResult.getSyncErrorMsg());
            default -> throw new SystemUnknownErrorException();
        }
    }

    /// 同步成功后更新资金凭证与容器
    private void success(PayTrade trade, NormalPayOrder normalOrder, PaySyncResultBo syncResult) {
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(syncResult.getFinishTime());
        trade.setCloseTime(null);
        trade.setErrorMsg(null);
        if (Objects.nonNull(syncResult.getProvider())) {
            trade.setProvider(syncResult.getProvider().getCode());
        }
        trade.setBuyerId(syncResult.getBuyerId());
        trade.setTradeProduct(syncResult.getTradeProduct());
        trade.setTradeWay(syncResult.getTradeWay());
        trade.setBankType(syncResult.getBankType());
        trade.setPromotionType(syncResult.getPromotionType());
        payUniHandleService.paySuccess(trade);
    }

    /// 主动关闭网关交易(超时场景), 调用通道关闭策略后统一关闭本地
    private void closeRemote(PayTrade trade, NormalPayOrder normalOrder) {
        AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                trade.getProduct(), AbsPayCloseStrategy.class);
        var context = new PayStrategyContext()
                .setContainer(normalOrder)
                .setTrade(trade);
        strategy.doBeforeClose(context);
        strategy.doClose(context, false);
        payUniHandleService.payClose(trade, normalOrder, false);
    }

    /// 保存同步流水记录
    private void saveRecord(PayTrade trade, NormalPayOrder normalOrder, PaySyncResultBo syncResult, boolean adjust) {
        PaySyncRecord record = new PaySyncRecord()
                .setAppId(trade.getAppId())
                .setTradeNo(trade.getTradeNo())
                .setBizTradeNo(Objects.nonNull(normalOrder) ? normalOrder.getBizOrderNo() : null)
                .setOutTradeNo(trade.getOutOrderNo())
                .setOutTradeStatus(Objects.nonNull(syncResult.getPayStatus())
                        ? syncResult.getPayStatus().getCode() : null)
                .setTradeType(trade.getTradeType())
                .setProduct(trade.getProduct())
                .setChannel(trade.getChannel())
                .setSyncInfo(syncResult.getSyncData())
                .setAdjust(adjust)
                .setErrorCode(syncResult.getSyncErrorCode())
                .setErrorMsg(syncResult.getSyncErrorMsg());
        // 商户号显式赋值, 不依赖线程上下文自动填充(异步通知/定时任务等非HTTP场景上下文缺失会导致填充null)
        record.setMchNo(trade.getMchNo());
        paySyncRecordService.saveRecord(record);
    }
}
