package cn.daxpay.open.payment.core.trade.runtime.service.sync;

import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.core.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.core.trade.runtime.service.pay.PayUniHandleService;
import cn.daxpay.open.payment.gateway.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.gateway.entity.GatewayPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.record.entity.PaySyncRecord;
import cn.daxpay.open.payment.core.trade.record.service.PaySyncRecordService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPaySyncParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.exception.RepetitiveOperationException;
import cn.daxpay.open.platform.core.exception.system.SystemUnknownErrorException;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class PaySyncService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
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
                trade = payTradeManager.findByContainerId(normalOrder.getId(), PayTradeTypeEnum.NORMAL.getCode())
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
            ContainerInfo info = loadContainerInfo(trade);
            var context = new PayStrategyContext()
                    .setTrade(trade)
                    .setChannelMchNo(info.channelMchNo())
                    .setCapability(info.capability())
                    .setClientIp(info.clientIp());
            var syncStrategy = PaymentStrategyFactory.createByProduct(
                    info.product(), AbsSyncPayOrderStrategy.class);
            PaySyncResultBo syncResult = syncStrategy.doSync(context);
            if (!Objects.equals(syncResult.getOutOrderNo(), trade.getOutOrderNo())) {
                trade.setOutOrderNo(syncResult.getOutOrderNo());
                payTradeManager.updateById(trade);
            }
            boolean statusSync = this.checkAndAdjust(syncResult, trade, info);
            if (!statusSync) {
                try {
                    this.adjustHandler(syncResult, trade, info);
                } catch (PayFailureException e) {
                    syncResult.setSyncSuccess(false).setSyncErrorMsg(e.getMessage());
                }
            }
            this.saveRecord(trade, syncResult, !statusSync, info);
            return new NormalPaySyncResult()
                    .setOrderStatus(trade.getStatus())
                    .setAdjust(statusSync);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    private boolean checkAndAdjust(PaySyncResultBo syncResult, PayTrade trade, ContainerInfo info) {
        var payStatus = Optional.ofNullable(syncResult.getPayStatus())
                .orElse(PayFundStatusEnum.PROCESSING);
        String orderStatus = trade.getStatus();
        if (orderStatus.equals(PayFundStatusEnum.FAIL.getCode())) {
            return false;
        }
        if (orderStatus.equals(PayFundStatusEnum.PROCESSING.getCode())) {
            if (Objects.equals(PayFundStatusEnum.PROCESSING, payStatus)) {
                if (info.expiredTime() != null
                        && DateTimeUtil.le(info.expiredTime(), OffsetDateTime.now(ZoneOffset.UTC))) {
                    syncResult.setPayStatus(PayFundStatusEnum.CLOSE);
                    syncResult.setRemoteClose(true);
                    return false;
                }
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    private void adjustHandler(PaySyncResultBo syncResult, PayTrade trade, ContainerInfo info) {
        var payStatus = syncResult.getPayStatus();
        if (Objects.isNull(payStatus)) {
            return;
        }
        switch (payStatus) {
            case PROCESSING -> {}
            case SUCCESS -> this.success(trade, syncResult);
            case CLOSE -> {
                if (syncResult.isRemoteClose()) {
                    this.closeRemote(trade, info);
                } else {
                    payUniHandleService.payClose(trade, false);
                }
            }
            case FAIL -> payUniHandleService.payFail(trade, syncResult.getSyncErrorMsg());
            default -> throw new SystemUnknownErrorException();
        }
    }

    private void success(PayTrade trade, PaySyncResultBo syncResult) {
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(syncResult.getFinishTime());
        trade.setCloseTime(null);
        // 通道回执写容器, 由 payUniHandleService 统一处理
        payUniHandleService.paySuccess(trade, syncResult);
    }

    private void closeRemote(PayTrade trade, ContainerInfo info) {
        var context = new PayStrategyContext()
                .setTrade(trade)
                .setChannelMchNo(info.channelMchNo())
                .setCapability(info.capability())
                .setClientIp(info.clientIp());
        AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                info.product(), AbsPayCloseStrategy.class);
        strategy.doBeforeClose(context);
        strategy.doClose(context, false);
        payUniHandleService.payTimeout(trade);
    }

    private void saveRecord(PayTrade trade, PaySyncResultBo syncResult, boolean adjust, ContainerInfo info) {
        PaySyncRecord record = new PaySyncRecord()
                .setAppId(trade.getAppId())
                .setTradeNo(trade.getTradeNo())
                .setBizTradeNo(info.bizOrderNo())
                .setOutTradeNo(trade.getOutOrderNo())
                .setOutTradeStatus(Objects.nonNull(syncResult.getPayStatus())
                        ? syncResult.getPayStatus().getCode() : null)
                .setTradeType(trade.getTradeType())
                .setProduct(info.product())
                .setChannel(info.channel())
                .setSyncInfo(syncResult.getSyncData())
                .setAdjust(adjust)
                .setErrorCode(syncResult.getSyncErrorCode())
                .setErrorMsg(syncResult.getSyncErrorMsg());
        record.setMchNo(trade.getMchNo());
        paySyncRecordService.saveRecord(record);
    }

    /// 从容器(normal/gateway)提取同步流程所需字段
    private ContainerInfo loadContainerInfo(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                return new ContainerInfo(
                        order.getProduct(), order.getChannel(), order.getBizOrderNo(),
                        order.getExpiredTime(), order.getChannelMchNo(),
                        order.getCapability(), order.getClientIp());
            }
        } else {
            NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                return new ContainerInfo(
                        order.getProduct(), order.getChannel(), order.getBizOrderNo(),
                        order.getExpiredTime(), order.getChannelMchNo(),
                        order.getCapability(), order.getClientIp());
            }
        }
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
    }

    /// 容器信息持有者(避免在各方法间传整个容器对象)
    private record ContainerInfo(
            String product, String channel, String bizOrderNo,
            OffsetDateTime expiredTime, String channelMchNo,
            String capability, String clientIp) {}
}
