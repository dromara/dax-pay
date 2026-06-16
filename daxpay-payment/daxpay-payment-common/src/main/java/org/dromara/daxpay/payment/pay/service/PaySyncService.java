package org.dromara.daxpay.payment.pay.service;

import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.PayFailureException;
import org.dromara.daxpay.platform.core.exception.RepetitiveOperationException;
import org.dromara.daxpay.platform.core.exception.system.SystemUnknownErrorException;
import org.dromara.daxpay.platform.core.util.DateTimeUtil;
import org.dromara.daxpay.payment.common.enums.PayFundStatusEnum;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.pay.bo.PaySyncResultBo;
import org.dromara.daxpay.payment.pay.order.dao.PayNormalOrderManager;
import org.dromara.daxpay.payment.pay.order.entity.PayNormalOrder;
import org.dromara.daxpay.payment.pay.order.dao.PayTradeManager;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
import org.dromara.daxpay.payment.strategy.sync.AbsSyncPayOrderStrategy;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PaySyncParam;
import org.dromara.daxpay.payment.unipay.result.trade.pay.PaySyncResult;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {

    private final PayTradeManager payTradeManager;
    private final PayNormalOrderManager payNormalOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;

    /// 支付同步
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult sync(PaySyncParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())
                && Objects.isNull(param.getOutOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayTrade trade = null;
        if (StrUtil.isNotBlank(param.getOrderNo())) {
            trade = payTradeManager.findByTradeNo(param.getOrderNo(), param.getAppId()).orElse(null);
        }
        if (Objects.isNull(trade) && Objects.nonNull(param.getBizOrderNo())) {
            PayNormalOrder normalOrder = payNormalOrderManager.findByBizOrderNo(
                    param.getBizOrderNo(), param.getAppId()).orElse(null);
            if (Objects.nonNull(normalOrder)) {
                trade = payTradeManager.findByContainerId(normalOrder.getId(), param.getAppId())
                        .orElse(null);
            }
        }
        if (Objects.isNull(trade) && StrUtil.isNotBlank(param.getOutOrderNo())) {
            trade = payTradeManager.findByOutOrderNo(param.getOutOrderNo(), param.getAppId())
                    .orElse(null);
        }
        if (Objects.isNull(trade)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
        }
        return this.syncPayOrder(trade);
    }

    /// 同步支付状态
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public PaySyncResult syncPayOrder(PayTrade trade) {
        if (Objects.equals(trade.getStatus(), PayFundStatusEnum.INIT.getCode())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.syncNotStarted");
        }
        LockInfo lock = lockTemplate.lock("sync:pay:" + trade.getId(), 10000, 200);
        if (Objects.isNull(lock)) {
            throw new RepetitiveOperationException();
        }
        try {
            PayNormalOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElse(null);
            var syncStrategy = PaymentStrategyFactory.createByProduct(
                    trade.getProduct(), AbsSyncPayOrderStrategy.class);
            syncStrategy.setTrade(trade);
            PaySyncResultBo syncResult = syncStrategy.doSync();
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
            return new PaySyncResult()
                    .setOrderStatus(trade.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 判断状态是否一致
    private boolean checkAndAdjust(PaySyncResultBo syncResult, PayTrade trade) {
        var payStatus = Optional.ofNullable(syncResult.getPayStatus())
                .orElse(PayFundStatusEnum.PROGRESS);
        String orderStatus = trade.getStatus();
        if (orderStatus.equals(PayFundStatusEnum.FAIL.getCode())) {
            return false;
        }
        if (orderStatus.equals(PayFundStatusEnum.PROGRESS.getCode())) {
            if (Objects.equals(PayFundStatusEnum.PROGRESS, payStatus)) {
                if (DateTimeUtil.le(trade.getExpiredTime(), OffsetDateTime.now(ZoneOffset.UTC))) {
                    syncResult.setPayStatus(PayFundStatusEnum.CLOSE);
                    return false;
                }
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    /// 根据同步结果调整支付单状态
    private void adjustHandler(PaySyncResultBo syncResult, PayTrade trade, PayNormalOrder normalOrder) {
        var payStatus = syncResult.getPayStatus();
        if (Objects.isNull(payStatus)) {
            return;
        }
        switch (payStatus) {
            case PROGRESS -> {}
            case SUCCESS -> this.success(trade, normalOrder, syncResult);
            case CLOSE -> payUniHandleService.payClose(trade, normalOrder);
            case FAIL -> payUniHandleService.payFail(trade, normalOrder, syncResult.getSyncErrorMsg());
            default -> throw new SystemUnknownErrorException();
        }
    }

    /// 同步成功后更新
    private void success(PayTrade trade, PayNormalOrder normalOrder, PaySyncResultBo syncResult) {
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
}
