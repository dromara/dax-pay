package cn.daxpay.open.payment.core.trade.service;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.PayFailureException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.payment.core.strategy.pay.PayStrategyContext;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.core.trade.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.record.entity.PayCloseRecord;
import cn.daxpay.open.payment.core.trade.record.service.PayCloseRecordService;
import cn.daxpay.open.payment.core.strategy.pay.AbsPayCloseStrategy;
import cn.daxpay.open.payment.unipay.param.trade.pay.NormalPayCloseParam;
import cn.daxpay.open.platform.core.enums.pay.pay.CloseTypeEnum;
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
/// 关闭(close): 本地置 CLOSE; 撤销(cancel): 本地置 CANCEL, 并通过 useCancel 触发通道撤销
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final PayCloseRecordService payCloseRecordService;
    private final LockTemplate lockTemplate;

    /// 关闭支付
    public void close(NormalPayCloseParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayTrade trade = payTradeManager.findByTradeNo(param.getOrderNo())
                .orElse(null);
        if (Objects.isNull(trade) && Objects.nonNull(param.getBizOrderNo())) {
            NormalPayOrder normalOrder = payNormalOrderManager.findByBizOrderNo(param.getBizOrderNo())
                    .orElse(null);
            if (Objects.nonNull(normalOrder)) {
                trade = payTradeManager.findByContainerId(normalOrder.getId())
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
            NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElse(null);
            CloseTypeEnum closeType;
            if (Objects.equals(PayFundStatusEnum.INIT.getCode(), trade.getStatus())) {
                // 待支付无通道交易, 仅本地关闭
                closeType = CloseTypeEnum.CLOSE;
                payUniHandleService.payClose(trade, normalOrder, false);
            } else {
                // 处理中: 调用通道关闭/撤销策略
                closeType = useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE;
                var context = new PayStrategyContext()
                        .setContainer(normalOrder)
                        .setTrade(trade);
                AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                        trade.getProduct(), AbsPayCloseStrategy.class);
                strategy.doBeforeClose(context);
                strategy.doClose(context, useCancel);
                payUniHandleService.payClose(trade, normalOrder, useCancel);
            }
            this.saveRecord(trade, normalOrder, closeType, null);
        } catch (Exception e) {
            log.error("关闭订单失败, id: {}:", trade.getId(), e);
            this.saveRecord(trade, null, useCancel ? CloseTypeEnum.CANCEL : CloseTypeEnum.CLOSE, e.getMessage());
            if (e instanceof PayFailureException) {
                throw e;
            }
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.pay.closeFailed");
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 超时自动关单(幂等)
    ///
    /// 供 MQ 延时消息消费者 [cn.daxpay.open.payment.core.trade.mq.NormalPayTimeoutConsumer]
    /// 与兜底定时任务 [cn.daxpay.open.payment.core.trade.job.NormalPayTimeoutJob] 调用。
    ///
    /// 与 [closeOrder] 的区别:
    /// - 仅接受 tradeNo 定位, 非 PROCESSING 状态静默返回(幂等, 不抛异常)
    /// - closeType 固定 TIMEOUT, 容器态置 EXPIRED(见 [PayUniHandleService#payTimeout])
    /// - 不接受 useCancel, 超时统一走 close
    /// - 通道关单失败不阻断本地关闭, 仅记录失败原因, 通道侧由后续同步兜底
    public void closeForTimeout(String tradeNo) {
        PayTrade trade = payTradeManager.findByTradeNo(tradeNo).orElse(null);
        if (Objects.isNull(trade)) {
            // 订单不存在, 静默返回
            return;
        }
        // 幂等校验: 仅处理中需超时关闭; 已成功/失败/关闭/撤销 直接返回
        if (!Objects.equals(PayFundStatusEnum.PROCESSING.getCode(), trade.getStatus())) {
            return;
        }
        // 复用与手动关单相同的锁键, 保证两条路径互斥
        LockInfo lock = lockTemplate.lock("payment:close:" + trade.getId(), 10000, 50);
        if (Objects.isNull(lock)) {
            // 并发关单进行中, 当前触发交由兜底任务后续补救
            log.warn("超时关单获取锁失败(并发关单进行中), 交由兜底任务处理, tradeNo={}", tradeNo);
            return;
        }
        try {
            // 加锁后再次校验状态(防止加锁期间已被其他路径处理)
            trade = payTradeManager.findByTradeNo(tradeNo).orElse(null);
            if (Objects.isNull(trade)
                    || !Objects.equals(PayFundStatusEnum.PROCESSING.getCode(), trade.getStatus())) {
                return;
            }
            NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElse(null);
            String errMsg = null;
            try {
                // 处理中: 调用通道关闭策略(超时统一走 close, 不走 cancel)
                var context = new PayStrategyContext()
                        .setContainer(normalOrder)
                        .setTrade(trade);
                AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                        trade.getProduct(), AbsPayCloseStrategy.class);
                strategy.doBeforeClose(context);
                strategy.doClose(context, false);
            } catch (Exception e) {
                // 通道关单失败不阻断本地关闭, 通道侧状态由后续同步兜底
                errMsg = e.getMessage();
                log.warn("超时关单调用通道关闭失败, 仅本地关闭, tradeNo={}", tradeNo, e);
            }
            payUniHandleService.payTimeout(trade, normalOrder);
            this.saveRecord(trade, normalOrder, CloseTypeEnum.TIMEOUT, errMsg);
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 保存关闭记录
    private void saveRecord(PayTrade trade, NormalPayOrder normalOrder, CloseTypeEnum closeType, String errMsg) {
        PayCloseRecord record = new PayCloseRecord()
                .setAppId(trade.getAppId())
                .setTradeNo(trade.getTradeNo())
                .setBizTradeNo(Objects.nonNull(normalOrder) ? normalOrder.getBizOrderNo() : null)
                .setProduct(trade.getProduct())
                .setChannel(trade.getChannel())
                .setClosed(StrUtil.isBlank(errMsg))
                .setCloseType(closeType.getCode())
                .setErrorMsg(errMsg);
        // 商户号显式赋值, 不依赖线程上下文自动填充(异步通知/定时任务等非HTTP场景上下文缺失会导致填充null)
        record.setMchNo(trade.getMchNo());
        payCloseRecordService.saveRecord(record);
    }
}
