package cn.daxpay.open.payment.core.trade.runtime.service.refund;

import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.CommonErrorCode;
import cn.daxpay.open.platform.core.code.DaxPayErrorCode;
import cn.daxpay.open.platform.core.exception.BizInfoException;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.core.util.TradeNoGenerateUtil;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.common.enums.RefundOrderStatusEnum;
import cn.daxpay.open.payment.core.strategy.PaymentStrategyFactory;
import cn.daxpay.open.payment.core.strategy.refund.AbsRefundStrategy;
import cn.daxpay.open.payment.core.trade.runtime.bo.RefundResultBo;
import cn.daxpay.open.payment.core.trade.order.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayRefundOrderManager;
import cn.daxpay.open.payment.core.trade.order.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.order.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayRefundOrder;
import cn.daxpay.open.payment.core.trade.order.entity.PayTrade;
import cn.daxpay.open.payment.core.trade.runtime.param.PayRefundParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/// # 退款服务
///
/// 退款编排: 查找原支付订单 → 校验可退 → 创建退款单 → 调用通道退款策略 → 回写状态与可退余额。
/// 参照 [PayCloseService] 的锁与编排模式。
@Slf4j
@Service
@RequiredArgsConstructor
public class PayRefundService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final PayRefundOrderManager payRefundOrderManager;
    private final LockTemplate lockTemplate;

    /// 发起退款
    public PayRefundOrder refund(PayRefundParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && StrUtil.isBlank(param.getBizOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }

        // 查找原支付交易
        PayTrade trade = resolveTrade(param.getOrderNo(), param.getBizOrderNo());

        // 校验可退状态
        validateRefundable(trade, param.getAmount());

        // 分布式锁, 防止并发退款导致可退余额超扣
        LockInfo lock = lockTemplate.lock("payment:refund:" + trade.getId(), 10000, 50);
        if (Objects.isNull(lock)) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.refund.processing");
        }
        try {
            // 二次校验可退余额(持锁后)
            PayTrade lockedTrade = payTradeManager.findById(trade.getId()).orElseThrow();
            validateRefundable(lockedTrade, param.getAmount());

            NormalPayOrder normalOrder = payNormalOrderManager.findById(lockedTrade.getContainerId()).orElse(null);

            // 创建退款单
            PayRefundOrder refundOrder = buildRefundOrder(lockedTrade, normalOrder, param);
            refundOrder.setStatus(RefundOrderStatusEnum.PROGRESS.getCode());
            payRefundOrderManager.save(refundOrder);

            // 调用通道退款策略(product 从容器获取)
            String product = normalOrder != null ? normalOrder.getProduct() : null;
            AbsRefundStrategy strategy = PaymentStrategyFactory.createByProduct(
                    product, AbsRefundStrategy.class);
            RefundResultBo result;
            try {
                strategy.doBeforeRefund(refundOrder);
                result = strategy.doRefund(refundOrder);
            } catch (Exception e) {
                log.error("通道退款失败, refundNo={}", refundOrder.getRefundNo(), e);
                refundOrder.setStatus(RefundOrderStatusEnum.FAIL.getCode());
                refundOrder.setErrorMsg(StrUtil.maxLength(e.getMessage(), 500));
                payRefundOrderManager.updateById(refundOrder);
                throw e;
            }

            // 回写退款单状态与可退余额
            applyRefundResult(refundOrder, lockedTrade, result);
            return refundOrder;
        } catch (Exception e) {
            if (e instanceof BizInfoException) {
                throw e;
            }
            log.error("退款处理失败, orderNo={}", param.getOrderNo(), e);
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.operateFailed");
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }

    /// 解析原支付交易(优先 orderNo, 其次 bizOrderNo)
    private PayTrade resolveTrade(String orderNo, String bizOrderNo) {
        if (StrUtil.isNotBlank(orderNo)) {
            return payTradeManager.findByTradeNo(orderNo)
                    .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
        }
        // 按 bizOrderNo 查容器, 再查关联交易
        NormalPayOrder normalOrder = payNormalOrderManager.findByBizOrderNo(bizOrderNo)
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
        return payTradeManager.findByContainerId(normalOrder.getId(),
                cn.daxpay.open.payment.common.enums.PayTradeTypeEnum.NORMAL.getCode())
                .orElseThrow(() -> new BizInfoException(DaxPayErrorCode.TRADE_STATUS_ERROR, "pay.error.notExists"));
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

    /// 构建退款订单(从原支付交易与容器复制路由信息)
    private PayRefundOrder buildRefundOrder(PayTrade trade, NormalPayOrder normalOrder, PayRefundParam param) {
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
        if (normalOrder != null) {
            refundOrder.setChannel(normalOrder.getChannel())
                    .setProduct(normalOrder.getProduct())
                    .setMethod(normalOrder.getMethod())
                    .setTitle(normalOrder.getTitle())
                    .setBizOrderNo(normalOrder.getBizOrderNo())
                    .setChannelMchNo(normalOrder.getChannelMchNo())
                    .setCapability(normalOrder.getCapability())
                    .setNotifyUrl(normalOrder.getNotifyUrl());
        }
        // 客户端IP: 优先取下单时留存的原订单IP, 为空(历史订单/非HTTP场景)则从当前HTTP请求兜底
        String refundClientIp = normalOrder != null ? normalOrder.getClientIp() : null;
        if (StrUtil.isBlank(refundClientIp)) {
            refundClientIp = WebServletUtil.getClientIp();
        }
        refundOrder.setClientIp(refundClientIp);
        return refundOrder;
    }

    /// 回写退款结果: 更新退款单状态 + 扣减可退余额
    private void applyRefundResult(PayRefundOrder refundOrder, PayTrade trade, RefundResultBo result) {
        refundOrder.setStatus(result.getStatus().getCode());
        if (result.getFinishTime() != null) {
            refundOrder.setFinishTime(result.getFinishTime());
        }
        if (StrUtil.isNotBlank(result.getOutRefundNo())) {
            refundOrder.setOutRefundNo(result.getOutRefundNo());
        }
        // 仅退款成功时扣减可退余额
        if (Objects.equals(result.getStatus(), RefundOrderStatusEnum.SUCCESS)) {
            long newBalance = (trade.getRefundableBalance() == null ? 0 : trade.getRefundableBalance())
                    - refundOrder.getAmount();
            trade.setRefundableBalance(Math.max(newBalance, 0));
            payTradeManager.updateById(trade);
        }
        payRefundOrderManager.updateById(refundOrder);
    }
}
