package cn.daxpay.open.payment.trade.runtime.service.close;

import cn.daxpay.open.payment.common.context.PaymentContext;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.record.entity.PayCloseRecord;
import cn.daxpay.open.payment.trade.record.service.PayCloseRecordService;
import cn.daxpay.open.payment.trade.runtime.service.pay.PayUniHandleService;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayCloseParam;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/// # 支付关闭和撤销服务
///
/// 所属 `runtime/close` 超时关单领域, 该领域内聚了三部分:
/// - service/ — 关单与超时关单服务(本类 + GatewayTimeoutService)
/// - consumer/ — Artemis 延时消息消费者(NormalPayTimeoutConsumer, GatewayTimeoutConsumer)
/// - job/ — 兜底定时任务(NormalPayTimeoutJob, GatewayTimeoutJob)
///
/// 消息契约(消息体 + 队列常量)位于 `runtime/mq/`, 作为 pay 注册方与 close 消费方的共享层,
/// 避免 pay → close 的跨领域环依赖。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final PayCloseRecordService payCloseRecordService;
    private final LockTemplate lockTemplate;
    private final PaymentContext paymentContext;

    /// 关闭支付(商户 API)
    public void close(NormalPayCloseParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayTrade trade = null;
        if (StrUtil.isNotBlank(param.getOrderNo())) {
            trade = payTradeManager.findByTradeNo(param.getOrderNo()).orElse(null);
        }
        if (Objects.isNull(trade) && Objects.nonNull(param.getBizOrderNo())) {
            NormalPayOrder normalOrder = payNormalOrderManager.findByBizOrderNo(param.getBizOrderNo())
                    .orElse(null);
            if (Objects.nonNull(normalOrder)) {
                trade = payTradeManager.findByContainerId(normalOrder.getId(), PayTradeTypeEnum.NORMAL.getCode())
                        .orElse(null);
            }
        }
        if (Objects.isNull(trade)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
        }
        this.closeOrder(trade, param.isUseCancel());
    }

    /// 关闭支付记录
    public void closeOrder(PayTrade trade, boolean useCancel) {
        if (!List.of(PayFundStatusEnum.INIT.getCode(), PayFundStatusEnum.PROCESSING.getCode())
                .contains(trade.getStatus())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.closeNotPaying");
        }
        LockInfo lock = lockTemplate.lock("payment:close:" + trade.getId(), 10000, 50);
        if (Objects.isNull(lock)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.closeProcessing");
        }
        try {
            // 持锁后二次读取状态, 避免与回调成功竞态把已成功单关掉
            PayTrade locked = payTradeManager.findById(trade.getId()).orElse(null);
            if (Objects.isNull(locked)) {
                throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
            }
            if (!List.of(PayFundStatusEnum.INIT.getCode(), PayFundStatusEnum.PROCESSING.getCode())
                    .contains(locked.getStatus())) {
                // 状态已变(如回调已成功), 不写失败关单记录
                throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.pay.closeNotPaying");
            }
            trade = locked;
            try {
                CloseTypeEnum closeType;
                if (Objects.equals(PayFundStatusEnum.INIT.getCode(), trade.getStatus())) {
                    closeType = CloseTypeEnum.CLOSE;
                    payUniHandleService.payClose(trade, false);
                } else {
                    closeType = useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
                    ContainerInfo info = loadContainerInfo(trade);
                    var context = new PayStrategyContext()
                            .setTrade(trade)
                            .setChannelMchNo(info.channelMchNo())
                            .setCapability(info.capability())
                            .setChannelAppId(info.channelAppId())
                            .setClientIp(info.clientIp());
                    AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                            info.product(), AbsPayCloseStrategy.class);
                    strategy.doBeforeClose(context);
                    strategy.doClose(context, useCancel);
                    payUniHandleService.payClose(trade, useCancel);
                }
                this.saveRecord(trade, closeType, null);
            } catch (Exception e) {
                log.error("关闭订单失败, id: {}:", trade.getId(), e);
                this.saveRecord(trade, useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE, e.getMessage());
                if (e instanceof PayFailureException || e instanceof BizInfoException) {
                    throw e;
                }
                throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.pay.closeFailed");
            }
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 超时自动关单(幂等)
    ///
    /// 定时/MQ 场景无商户登录上下文：引导读用 NotTenant，再按交易 mchNo 装载后走租户内关单。
    /// 仅 setMchNo，不校验商户启用（禁用商户历史单仍需超时关）。
    public void closeForTimeout(String tradeNo) {
        PayTrade boot = payTradeManager.findByTradeNoNotTenant(tradeNo).orElse(null);
        if (Objects.isNull(boot)) {
            return;
        }
        if (!Objects.equals(PayFundStatusEnum.PROCESSING.getCode(), boot.getStatus())) {
            return;
        }
        if (StrUtil.isBlank(boot.getMchNo())) {
            log.error("超时关单交易缺少 mchNo, tradeNo={}", tradeNo);
            return;
        }
        LockInfo lock = lockTemplate.lock("payment:close:" + boot.getId(), 10000, 50);
        if (Objects.isNull(lock)) {
            log.warn("超时关单获取锁失败(并发关单进行中), 交由兜底任务处理, tradeNo={}", tradeNo);
            return;
        }
        try {
            paymentContext.runAs(() -> {
                paymentContext.setMchNo(boot.getMchNo());
                PayTrade trade = payTradeManager.findByTradeNo(tradeNo).orElse(null);
                if (Objects.isNull(trade)
                        || !Objects.equals(PayFundStatusEnum.PROCESSING.getCode(), trade.getStatus())) {
                    return;
                }
                String errMsg = null;
                try {
                    ContainerInfo info = loadContainerInfo(trade);
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
                } catch (Exception e) {
                    errMsg = e.getMessage();
                    log.warn("超时关单调用通道关闭失败, 仅本地关闭, tradeNo={}", tradeNo, e);
                }
                payUniHandleService.payTimeout(trade);
                this.saveRecord(trade, CloseTypeEnum.TIMEOUT, errMsg);
            });
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    private void saveRecord(PayTrade trade, CloseTypeEnum closeType, String errMsg) {
        ContainerInfo info = loadContainerInfo(trade);
        PayCloseRecord record = new PayCloseRecord()
                .setAppId(trade.getAppId())
                .setTradeNo(trade.getTradeNo())
                .setBizTradeNo(info.bizOrderNo())
                .setProduct(info.product())
                .setChannel(info.channel())
                .setClosed(StrUtil.isBlank(errMsg))
                .setCloseType(closeType.getCode())
                .setErrorMsg(errMsg);
        record.setMchNo(trade.getMchNo());
        payCloseRecordService.saveRecord(record);
    }

    /// 从容器(normal/gateway)提取关闭流程所需字段
    private ContainerInfo loadContainerInfo(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                return new ContainerInfo(
                        order.getProduct(), order.getChannel(), order.getBizOrderNo(),
                        order.getChannelMchNo(), order.getCapability(), order.getChannelAppId(), order.getClientIp());
            }
        } else {
            NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId()).orElse(null);
            if (order != null) {
                return new ContainerInfo(
                        order.getProduct(), order.getChannel(), order.getBizOrderNo(),
                        order.getChannelMchNo(), order.getCapability(), order.getChannelAppId(), order.getClientIp());
            }
        }
        throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.payOrderNotExist");
    }

    private record ContainerInfo(
            String product, String channel, String bizOrderNo,
            String channelMchNo, String capability, String channelAppId, String clientIp) {}
}
