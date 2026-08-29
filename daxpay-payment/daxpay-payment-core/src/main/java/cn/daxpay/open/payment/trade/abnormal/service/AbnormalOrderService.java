package cn.daxpay.open.payment.trade.abnormal.service;

import cn.daxpay.open.payment.trade.abnormal.dao.AbnormalOrderManager;
import cn.daxpay.open.payment.trade.abnormal.entity.AbnormalOrder;
import cn.daxpay.open.payment.trade.abnormal.enums.AbnormalHandleStatusEnum;
import cn.daxpay.open.payment.trade.abnormal.enums.AbnormalOrderTypeEnum;
import cn.daxpay.open.payment.trade.abnormal.enums.AbnormalSourceEnum;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.bo.CallbackData;
import cn.daxpay.open.payment.trade.runtime.bo.PaySyncResultBo;
import cn.daxpay.open.payment.trade.runtime.service.pay.common.PayUniHandleService;
import cn.daxpay.open.platform.common.json.util.JsonUtil;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/// # 异常订单服务
///
/// 终态订单(FAIL/CLOSE/CANCEL)收到通道收款证据时落异常台账, 不自动翻转资金状态(2026-08-29 决策),
/// 由运营核实通道后人工「确认成功」(走 [PayUniHandleService#confirmPaySuccess] 统一补通知/插件/风控)或「忽略」。
///
/// 幂等: 同一交易同时最多一条待处理异常单(pending 部分唯一索引兜底),
/// 已存在时仅刷新通道核实结果(channelStatus/outOrderNo), 不重复插入。
///
/// 确认走「锁外层 + 事务内层」: [confirmSuccess] 持 `payment:trade:{id}` 锁(与回调/同步/关单互斥)后,
/// [doConfirmSuccess] 经 self 代理走事务, 消除"锁释放早于事务提交"窗口。
@Slf4j
@Service
@RequiredArgsConstructor
public class AbnormalOrderService {

    private final AbnormalOrderManager abnormalOrderManager;
    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final LockExecutor lockExecutor;

    /// 自注入: 保证 [doConfirmSuccess] 走 Spring 事务代理
    @Lazy
    private final AbnormalOrderService self;

    /// 回调路径: 终态单收到支付成功回调, 落异常订单
    public void recordFromCallback(PayTrade trade, CallbackData callbackData) {
        Map<String, ?> notifyData = Optional.ofNullable(callbackData.getCallbackData()).orElse(Map.of());
        this.record(trade, AbnormalSourceEnum.CALLBACK, callbackData.getOutTradeNo(), JsonUtil.toJsonStr(notifyData));
    }

    /// 同步路径: 终态单查单发现通道已收款, 落异常订单
    public void recordFromSync(PayTrade trade, PaySyncResultBo syncResult) {
        this.record(trade, resolveSyncSource(syncResult), syncResult.getOutOrderNo(), null);
    }

    /// 人工确认成功(锁外层)。返回给调用方留痕
    public void confirmSuccess(Long id, String handler, String remark) {
        AbnormalOrder boot = abnormalOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.abnormal.notFound"));
        PayTrade trade = payTradeManager.findByTradeNoNotTenant(boot.getTradeNo())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        lockExecutor.run(
                "payment:trade:" + trade.getId(),
                () -> self.doConfirmSuccess(id, handler, remark),
                () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.pay.closeProcessing")
        );
    }

    /// 确认成功核心(锁内层, 事务边界), 通过 self 走代理保证 @Transactional 生效
    @Transactional(rollbackFor = Exception.class)
    public void doConfirmSuccess(Long id, String handler, String remark) {
        AbnormalOrder abnormal = abnormalOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.abnormal.notFound"));
        this.assertPending(abnormal);
        PayTrade trade = payTradeManager.findByTradeNoNotTenant(abnormal.getTradeNo())
                .orElseThrow(() -> new DataNotExistException("pay.error.payOrderNotExist"));
        // 已被其他路径翻成功(如回调竞态): 直接闭环异常单, 不再重复翻转
        if (!Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
            payUniHandleService.confirmPaySuccess(trade);
        }
        this.markHandled(abnormal, AbnormalHandleStatusEnum.CONFIRMED, "confirm_success", handler, remark);
        log.info("异常订单人工确认成功: tradeNo={}, handler={}", abnormal.getTradeNo(), handler);
    }

    /// 忽略异常单(核实无需入账, 如通道侧已原路退回)
    @Transactional(rollbackFor = Exception.class)
    public void ignore(Long id, String handler, String remark) {
        AbnormalOrder abnormal = abnormalOrderManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("pay.error.abnormal.notFound"));
        this.assertPending(abnormal);
        this.markHandled(abnormal, AbnormalHandleStatusEnum.IGNORED, "ignore", handler, remark);
        log.info("异常订单已忽略: tradeNo={}, handler={}", abnormal.getTradeNo(), handler);
    }

    /// 落/刷新异常订单(与调用方事务同生共死: 主流程回滚则不落, 回调重试可再落)
    private void record(PayTrade trade, AbnormalSourceEnum source, String outOrderNo, String notifyInfo) {
        AbnormalOrderTypeEnum type = AbnormalOrderTypeEnum.fromTradeStatus(trade.getStatus());
        Optional<AbnormalOrder> existed = abnormalOrderManager.findPendingByTradeNo(trade.getTradeNo());
        if (existed.isPresent()) {
            // 已有待处理单: 仅刷新通道核实结果
            AbnormalOrder refresh = existed.get()
                    .setChannelStatus(PayFundStatusEnum.SUCCESS.getCode());
            if (StrUtil.isNotBlank(outOrderNo)) {
                refresh.setOutOrderNo(outOrderNo);
            }
            abnormalOrderManager.updateById(refresh);
            return;
        }
        AbnormalOrder abnormal = new AbnormalOrder()
                .setTradeNo(trade.getTradeNo())
                .setBizOrderNo(this.resolveBizOrderNo(trade))
                .setTradeType(trade.getTradeType())
                .setTitle(trade.getTitle())
                .setAmount(trade.getAmount())
                .setCurrency(trade.getCurrency())
                .setTradeStatus(trade.getStatus())
                .setAbnormalType(type.getCode())
                .setSource(source.getCode())
                .setChannel(trade.getChannel())
                .setProvider(trade.getProvider())
                .setChannelMchNo(trade.getChannelMchNo())
                .setOutOrderNo(outOrderNo)
                .setChannelStatus(PayFundStatusEnum.SUCCESS.getCode())
                .setCallbackNotifyInfo(notifyInfo)
                .setHandleStatus(AbnormalHandleStatusEnum.PENDING.getCode())
                .setAppId(trade.getAppId());
        // 显式写入商户号, 回调/定时等无 PaymentContext 场景避免 Fill 缺失
        abnormal.setMchNo(trade.getMchNo());
        try {
            abnormalOrderManager.save(abnormal);
            log.warn("异常订单已记录: tradeNo={}, type={}, source={}", trade.getTradeNo(), type.getCode(), source.getCode());
        } catch (DuplicateKeyException e) {
            // 并发双落(回调+同步同时发现), 唯一索引兜底
            log.info("异常订单已存在, 幂等跳过: tradeNo={}", trade.getTradeNo());
        }
    }

    /// 定时任务同步入口的来源区分(见 [PaySyncService#syncPayOrderFromJob])
    private AbnormalSourceEnum resolveSyncSource(PaySyncResultBo syncResult) {
        if (syncResult.isFromJob()) {
            return AbnormalSourceEnum.JOB;
        }
        return AbnormalSourceEnum.SYNC;
    }

    /// 从容器(normal/gateway)取商户业务单号
    private String resolveBizOrderNo(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            return gatewayPayOrderManager.findById(trade.getContainerId())
                    .map(GatewayPayOrder::getBizOrderNo).orElse(null);
        }
        return payNormalOrderManager.findById(trade.getContainerId())
                .map(NormalPayOrder::getBizOrderNo).orElse(null);
    }

    /// 校验异常单待处理
    private void assertPending(AbnormalOrder abnormal) {
        if (!Objects.equals(abnormal.getHandleStatus(), AbnormalHandleStatusEnum.PENDING.getCode())) {
            // 异常订单已处置, 不可重复操作
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.abnormal.alreadyHandled");
        }
    }

    /// 回写处置结果
    private void markHandled(AbnormalOrder abnormal, AbnormalHandleStatusEnum status,
                             String action, String handler, String remark) {
        abnormal.setHandleStatus(status.getCode())
                .setHandleAction(action)
                .setHandler(handler)
                .setHandleTime(OffsetDateTime.now(ZoneOffset.UTC));
        if (StrUtil.isNotBlank(remark)) {
            abnormal.setHandleRemark(StrUtil.maxLength(remark, 300));
        }
        abnormalOrderManager.updateById(abnormal);
    }
}
