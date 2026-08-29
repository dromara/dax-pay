package cn.daxpay.open.payment.trade.runtime.service.sync;

import cn.daxpay.open.payment.trade.abnormal.service.AbnormalOrderService;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.strategy.sync.AbsSyncPayOrderStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.record.entity.PaySyncRecord;
import cn.daxpay.open.payment.trade.record.service.PaySyncRecordService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPaySyncParam;
import cn.daxpay.open.payment.unipay.result.trade.pay.NormalPaySyncResult;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.exception.RepetitiveOperationException;
import cn.daxpay.open.platform.core.exception.system.SystemUnknownErrorException;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.util.DateTimeUtil;
import cn.hutool.core.util.StrUtil;
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
    private final AbnormalOrderService abnormalOrderService;
    private final LockExecutor lockExecutor;

    /// 按容器ID同步支付状态
    ///
    /// 供容器视角的对外 Service(Admin/Merchant)调用, 内部反查资金凭证后委托 [syncPayOrder]。
    public NormalPaySyncResult syncByContainer(Long containerId, String tradeType) {
        PayTrade trade = payTradeManager.findByContainerId(containerId, tradeType)
                .orElseThrow(() -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist"));
        return this.doSyncPayOrder(trade, false);
    }

    /// 支付同步
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public NormalPaySyncResult sync(NormalPaySyncParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())
                && Objects.isNull(param.getOutOrderNo())) {
            // 支付: 支付订单号不能都为空
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
            // 支付: 支付订单不存在
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
        }
        return this.doSyncPayOrder(trade, false);
    }

    /// 同步支付状态(手动同步入口, 异常订单发现来源记 sync)
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public NormalPaySyncResult syncPayOrder(PayTrade trade) {
        return this.doSyncPayOrder(trade, false);
    }

    /// 同步支付状态(定时任务入口, 异常订单发现来源记 job)
    ///
    /// 与 [#syncPayOrder] 逻辑一致, 区别仅在异常订单发现来源标记。
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public NormalPaySyncResult syncPayOrderFromJob(PayTrade trade) {
        return this.doSyncPayOrder(trade, true);
    }

    /// 同步执行主体(fromJob 仅影响异常订单发现来源标记, 事务语义由公开入口声明)
    private NormalPaySyncResult doSyncPayOrder(PayTrade trade, boolean fromJob) {
        if (Objects.equals(trade.getStatus(), PayFundStatusEnum.INIT.getCode())) {
            // 支付: 订单未开始支付请重新确认支付状态
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.syncNotStarted");
        }
        return lockExecutor.execute(
                "payment:trade:" + trade.getId(),
                () -> {
                    ContainerInfo info = loadContainerInfo(trade);
                    var context = new PayStrategyContext()
                            .setTrade(trade)
                            .setChannelMchNo(info.channelMchNo())
                            .setCapability(info.capability())
                            .setChannelAppId(info.channelAppId())
                            .setClientIp(info.clientIp());
                    var syncStrategy = PaymentStrategyFactory.createByProduct(
                            info.product(), AbsSyncPayOrderStrategy.class);
                    PaySyncResultBo syncResult = syncStrategy.doSync(context);
                    syncResult.setFromJob(fromJob);
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
                    // statusSync=true 表示本地与通道已一致、无需调整; API adjust 表示「是否触发了调整」
                    boolean adjusted = !statusSync;
                    this.saveRecord(trade, syncResult, adjusted, info);
                    return new NormalPaySyncResult()
                            .setOrderStatus(trade.getStatus())
                            .setAdjust(adjusted);
                },
                RepetitiveOperationException::new
        );
    }

    /// 校验同步结果是否需要调整
    ///
    /// - PROCESSING 且未超时则无需调整
    /// - 已超时触发远程关单调整
    /// - 终态(FAIL/CLOSE/CANCEL) + 通道 SUCCESS: 不自动翻转(2026-08-29 决策), 落异常订单人工处置
    private boolean checkAndAdjust(PaySyncResultBo syncResult, PayTrade trade, ContainerInfo info) {
        var payStatus = Optional.ofNullable(syncResult.getPayStatus())
                .orElse(PayFundStatusEnum.PROCESSING);
        String orderStatus = trade.getStatus();
        if (Objects.equals(orderStatus, PayFundStatusEnum.PROCESSING.getCode())) {
            if (Objects.equals(PayFundStatusEnum.PROCESSING, payStatus)) {
                if (info.expiredTime() != null
                        && DateTimeUtil.le(info.expiredTime(), OffsetDateTime.now(ZoneOffset.UTC))) {
                    syncResult.setPayStatus(PayFundStatusEnum.CLOSE);
                    syncResult.setRemoteClose(true);
                    return false;
                }
                return true;
            }
            return false;
        }
        // 非支付中: 已 SUCCESS 或终态(FAIL/CLOSE/CANCEL)
        if (isTerminalAbnormal(orderStatus) && Objects.equals(PayFundStatusEnum.SUCCESS, payStatus)) {
            // 通道已收款但本地已终态: 转异常订单人工处置, 不自动翻转
            abnormalOrderService.recordFromSync(trade, syncResult);
            syncResult.setSyncErrorMsg("通道已收款但订单已终态(" + orderStatus + ")，已转异常订单处理");
            return true;
        }
        return true;
    }

    /// 终态异常集合(FAIL/CLOSE/CANCEL): 这些终态收到通道收款证据时转异常订单
    private static boolean isTerminalAbnormal(String status) {
        return Objects.equals(status, PayFundStatusEnum.FAIL.getCode())
                || Objects.equals(status, PayFundStatusEnum.CLOSE.getCode())
                || Objects.equals(status, PayFundStatusEnum.CANCEL.getCode());
    }

    /// 根据同步结果执行状态调整: SUCCESS→回写成功; CLOSE→远程或本地关单; FAIL→标记失败
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

    /// 将交易置为成功并回写支付时间, 通道回执交由 payUniHandleService 统一处理
    private void success(PayTrade trade, PaySyncResultBo syncResult) {
        trade.setStatus(PayFundStatusEnum.SUCCESS.getCode());
        trade.setPayTime(syncResult.getFinishTime());
        trade.setCloseTime(null);
        // 通道回执写容器, 由 payUniHandleService 统一处理
        payUniHandleService.paySuccess(trade, syncResult);
    }

    /// 远程关单: 调用通道关闭策略后本地超时关闭
    private void closeRemote(PayTrade trade, ContainerInfo info) {
        var context = new PayStrategyContext()
                .setTrade(trade)
                .setChannelMchNo(info.channelMchNo())
                .setCapability(info.capability())
                .setChannelAppId(info.channelAppId())
                .setClientIp(info.clientIp());
        AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                info.product(), AbsPayCloseStrategy.class);
        strategy.doBeforeClose(context);
        strategy.doClose(context, false);
        payUniHandleService.payTimeout(trade);
    }

    /// 记录支付同步流水(含通道同步快照与是否调整标记)
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
                        order.getCapability(), order.getChannelAppId(), order.getClientIp());
            }
        } else {
            NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                return new ContainerInfo(
                        order.getProduct(), order.getChannel(), order.getBizOrderNo(),
                        order.getExpiredTime(), order.getChannelMchNo(),
                        order.getCapability(), order.getChannelAppId(), order.getClientIp());
            }
        }
        // 支付: 支付订单不存在
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
    }

    /// 容器信息持有者(避免在各方法间传整个容器对象)
    private record ContainerInfo(
            String product, String channel, String bizOrderNo,
            OffsetDateTime expiredTime, String channelMchNo,
            String capability, String channelAppId, String clientIp) {}
}
