package cn.daxpay.open.payment.trade.runtime.service.refund;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.enums.PayTradeTypeEnum;
import cn.daxpay.open.payment.trade.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.trade.order.dao.GatewayPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.trade.order.entity.GatewayPayOrder;
import cn.daxpay.open.payment.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.trade.runtime.param.PayRefundParam;
import cn.daxpay.open.platform.common.redis.lock.LockExecutor;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 退款服务
///
/// 退款编排: 查找原支付交易 → 加载原支付容器快照 → 预占可退余额 → 创建退款单 → 调通道 → 终态结算。
/// 资金预占/成功/失败回滚委托 [PayRefundSettleService], 与同步/回调共用 trade 级锁。
/// 支持 [PayTradeTypeEnum#NORMAL] 与 [PayTradeTypeEnum#GATEWAY] 两种容器。
/// 通道/产品继承自原支付容器快照，不调用 [PayRouteService] 二次选路。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final GatewayPayOrderManager gatewayPayOrderManager;
    private final PayRefundOrderManager payRefundOrderManager;
    private final PayRefundSettleService payRefundSettleService;
    private final LockExecutor lockExecutor;

    /// 发起退款
    public PayRefundOrder refund(PayRefundParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && StrUtil.isBlank(param.getBizOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }

        // 查找原支付交易(支持 normal / gateway)
        PayTrade trade = resolveTrade(param.getOrderNo(), param.getBizOrderNo());

        // 校验可退状态
        validateRefundable(trade, param.getAmount());

        // 分布式锁: 预占与结算共用, 防止并发超退
        try {
            return lockExecutor.execute(
                    PayRefundSettleService.lockKey(trade.getTradeNo()),
                    10000,
                    50,
                    () -> {
                        // 二次校验可退余额(持锁后)
                        PayTrade lockedTrade = payTradeManager.findById(trade.getId()).orElseThrow();
                        validateRefundable(lockedTrade, param.getAmount());

                        // 按 tradeType 加载原支付容器快照(非二次路由)
                        RefundOrderSnapshot snapshot = loadContainerSnapshot(lockedTrade);

                        // 预占可退余额 + 创建退款单(progress)
                        payRefundSettleService.reserveBalanceUnderLock(lockedTrade, param.getAmount());
                        PayRefundOrder refundOrder = buildRefundOrder(lockedTrade, snapshot, param);
                        refundOrder.setStatus(RefundOrderStatusEnum.PROGRESS.getCode());
                        payRefundOrderManager.save(refundOrder);

                        // 调用通道退款策略(product 继承自原支付单)
                        AbsRefundStrategy strategy = PaymentStrategyFactory.createByProduct(
                                snapshot.getProduct(), AbsRefundStrategy.class);
                        RefundResultBo result;
                        try {
                            strategy.doBeforeRefund(refundOrder);
                            result = strategy.doRefund(refundOrder);
                        } catch (Exception e) {
                            log.error("通道退款失败, refundNo={}", refundOrder.getRefundNo(), e);
                            // 预占回滚 + 退款单 FAIL
                            payRefundSettleService.settleFailOrCloseUnderLock(
                                    refundOrder.getId(), false, null, null, e.getMessage());
                            throw e;
                        }

                        // 回写结果(SUCCESS 不二次扣; FAIL 回滚; PROGRESS 保持预占)
                        applyRefundResult(refundOrder, result);
                        return payRefundOrderManager.findById(refundOrder.getId()).orElse(refundOrder);
                    },
                    () -> new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.processing")
            );
        } catch (BizInfoException e) {
            throw e;
        } catch (Exception e) {
            log.error("退款处理失败, orderNo={}", param.getOrderNo(), e);
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.operateFailed");
        }
    }

    /// 解析原支付交易
    ///
    /// 优先 orderNo(= PayTrade.tradeNo); 若无 Trade 再尝试网关容器 orderNo。
    /// 仅 bizOrderNo 时: 先 Normal 再 Gateway。
    private PayTrade resolveTrade(String orderNo, String bizOrderNo) {
        if (StrUtil.isNotBlank(orderNo)) {
            var byTradeNo = payTradeManager.findByTradeNo(orderNo);
            if (byTradeNo.isPresent()) {
                return byTradeNo.get();
            }
            // 运营友好: 支持用网关 URL 单号反查
            GatewayPayOrder gateway = gatewayPayOrderManager.findByOrderNo(orderNo).orElse(null);
            if (gateway != null) {
                return payTradeManager.findByContainerId(gateway.getId(), PayTradeTypeEnum.GATEWAY.getCode())
                        .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
            }
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists");
        }

        // 按 bizOrderNo: Normal 优先, 再 Gateway
        NormalPayOrder normalOrder = payNormalOrderManager.findByBizOrderNo(bizOrderNo).orElse(null);
        if (normalOrder != null) {
            return payTradeManager.findByContainerId(normalOrder.getId(), PayTradeTypeEnum.NORMAL.getCode())
                    .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
        }
        GatewayPayOrder gatewayOrder = gatewayPayOrderManager.findByBizOrderNo(bizOrderNo).orElse(null);
        if (gatewayOrder != null) {
            return payTradeManager.findByContainerId(gatewayOrder.getId(), PayTradeTypeEnum.GATEWAY.getCode())
                    .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
        }
        throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists");
    }

    /// 按 tradeType 从原支付容器加载退款建单快照(继承通道/产品, 不二次路由)
    private RefundOrderSnapshot loadContainerSnapshot(PayTrade trade) {
        if (Objects.equals(trade.getTradeType(), PayTradeTypeEnum.GATEWAY.getCode())) {
            GatewayPayOrder order = gatewayPayOrderManager.findById(trade.getContainerId())
                    .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
            if (StrUtil.isBlank(order.getProduct())) {
                throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.statusNotAllow", trade.getStatus());
            }
            return new RefundOrderSnapshot()
                    .setProduct(order.getProduct())
                    .setChannel(order.getChannel())
                    .setMethod(order.getMethod())
                    .setChannelMchNo(order.getChannelMchNo())
                    .setCapability(order.getCapability())
                    .setChannelAppId(order.getChannelAppId())
                    .setTitle(order.getTitle())
                    .setBizOrderNo(order.getBizOrderNo())
                    .setNotifyUrl(order.getNotifyUrl())
                    .setClientIp(order.getClientIp())
                    .setAttach(order.getAttach());
        }
        // 默认普通支付(含 future 扩展未知类型时尽量按 normal 查, 查不到再失败)
        NormalPayOrder order = payNormalOrderManager.findById(trade.getContainerId())
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
        if (StrUtil.isBlank(order.getProduct())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.statusNotAllow", trade.getStatus());
        }
        return new RefundOrderSnapshot()
                .setProduct(order.getProduct())
                .setChannel(order.getChannel())
                .setMethod(order.getMethod())
                .setChannelMchNo(order.getChannelMchNo())
                .setCapability(order.getCapability())
                .setChannelAppId(order.getChannelAppId())
                .setTitle(order.getTitle())
                .setBizOrderNo(order.getBizOrderNo())
                .setNotifyUrl(order.getNotifyUrl())
                .setClientIp(order.getClientIp())
                .setAttach(order.getAttach());
    }

    /// 校验交易可退: 状态须为 SUCCESS, 退款金额不能超过可退余额
    private void validateRefundable(PayTrade trade, Long refundAmount) {
        if (!Objects.equals(PayFundStatusEnum.SUCCESS.getCode(), trade.getStatus())) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.statusNotAllow", trade.getStatus());
        }
        long refundable = trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance();
        if (refundAmount > refundable) {
            throw new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.refund.amountExceed");
        }
    }

    /// 构建退款订单(通道/产品/通知字段来自原支付容器快照)
    private PayRefundOrder buildRefundOrder(PayTrade trade, RefundOrderSnapshot snapshot, PayRefundParam param) {
        PayRefundOrder refundOrder = new PayRefundOrder();
        // setMchNo 继承自父类, 单独调用避免链式返回父类型
        refundOrder.setMchNo(trade.getMchNo());
        refundOrder.setAppId(trade.getAppId())
                .setRefundNo(TradeNoGenerateUtil.refund())
                .setBizRefundNo(StrUtil.blankToDefault(param.getBizRefundNo(), TradeNoGenerateUtil.refund()))
                .setOrderNo(trade.getTradeNo())
                .setOutOrderNo(trade.getOutOrderNo())
                .setAmount(param.getAmount())
                .setOrderAmount(trade.getAmount())
                .setCurrency(trade.getCurrency())
                .setReason(param.getReason());
        refundOrder.setChannel(snapshot.getChannel())
                .setProduct(snapshot.getProduct())
                .setMethod(snapshot.getMethod())
                .setTitle(snapshot.getTitle())
                .setBizOrderNo(snapshot.getBizOrderNo())
                .setChannelMchNo(snapshot.getChannelMchNo())
                .setCapability(snapshot.getCapability())
                .setChannelAppId(snapshot.getChannelAppId())
                // notifyUrl 语义: 商户出站通知地址, 通道回调 URL 由各通道 buildRefundNotifyUrl 生成
                .setNotifyUrl(snapshot.getNotifyUrl())
                .setAttach(snapshot.getAttach());
        // 客户端IP: 优先取下单时留存的原订单IP, 为空则从当前HTTP请求兜底
        String refundClientIp = snapshot.getClientIp();
        if (StrUtil.isBlank(refundClientIp)) {
            refundClientIp = WebServletUtil.getClientIp();
        }
        refundOrder.setClientIp(refundClientIp);
        return refundOrder;
    }

    /// 回写退款结果: SUCCESS 仅改态; FAIL 回滚预占; PROGRESS 保持预占
    private void applyRefundResult(PayRefundOrder refundOrder, RefundResultBo result) {
        if (result.getStatus() == null) {
            return;
        }
        if (Objects.equals(result.getStatus(), RefundOrderStatusEnum.SUCCESS)) {
            payRefundSettleService.settleSuccessUnderLock(
                    refundOrder.getId(), result.getFinishTime(), result.getOutRefundNo());
            return;
        }
        if (Objects.equals(result.getStatus(), RefundOrderStatusEnum.FAIL)
                || Objects.equals(result.getStatus(), RefundOrderStatusEnum.CLOSE)) {
            boolean close = Objects.equals(result.getStatus(), RefundOrderStatusEnum.CLOSE);
            payRefundSettleService.settleFailOrCloseUnderLock(
                    refundOrder.getId(), close, result.getFinishTime(), result.getOutRefundNo(), null);
            return;
        }
        // PROGRESS 等中间态: 补写通道退款号, 余额保持已预占
        payRefundSettleService.applyProgressResult(
                refundOrder, result.getFinishTime(), result.getOutRefundNo(), null);
    }
}
