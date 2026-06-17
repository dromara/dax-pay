package org.dromara.daxpay.payment.pay.service;

import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.code.CommonErrorCode;
import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.BizInfoException;
import org.dromara.daxpay.platform.core.exception.PayFailureException;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.payment.common.enums.PayFundStatusEnum;
import org.dromara.daxpay.payment.common.util.PaymentStrategyFactory;
import org.dromara.daxpay.payment.pay.order.dao.PayNormalOrderManager;
import org.dromara.daxpay.payment.pay.order.entity.PayNormalOrder;
import org.dromara.daxpay.payment.pay.order.dao.PayTradeManager;
import org.dromara.daxpay.payment.pay.order.entity.PayTrade;
import org.dromara.daxpay.payment.strategy.pay.AbsPayCloseStrategy;
import org.dromara.daxpay.payment.unipay.param.trade.pay.PayCloseParam;
import org.dromara.daxpay.platform.core.enums.pay.pay.CloseTypeEnum;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseService {

    private final PayTradeManager payTradeManager;
    private final PayNormalOrderManager payNormalOrderManager;
    private final PayUniHandleService payUniHandleService;
    private final LockTemplate lockTemplate;

    /// 关闭支付
    public void close(PayCloseParam param) {
        if (StrUtil.isBlank(param.getOrderNo()) && Objects.isNull(param.getBizOrderNo())) {
            throw new BizInfoException(CommonErrorCode.VALIDATE_PARAMETERS_ERROR, "pay.error.orderNoRequired");
        }
        PayTrade trade = payTradeManager.findByTradeNo(param.getOrderNo())
                .orElse(null);
        if (Objects.isNull(trade) && Objects.nonNull(param.getBizOrderNo())) {
            PayNormalOrder normalOrder = payNormalOrderManager.findByBizOrderNo(param.getBizOrderNo())
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
            PayNormalOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElse(null);
            if (Objects.equals(PayFundStatusEnum.INIT.getCode(), trade.getStatus())) {
                payUniHandleService.payClose(trade, normalOrder);
            } else {
                AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                        trade.getProduct(), AbsPayCloseStrategy.class);
                strategy.setTrade(trade);
                strategy.init(trade, useCancel);
                strategy.doBeforeCloseHandler();
                strategy.doCloseHandler();
                payUniHandleService.payClose(trade, normalOrder);
            }
        } catch (Exception e) {
            log.error("关闭订单失败, id: {}:", trade.getId(), e);
            if (e instanceof PayFailureException) {
                throw e;
            }
            throw new OperationFailException(CommonCode.FAIL_CODE, "pay.error.pay.closeFailed");
        } finally {
            lockTemplate.releaseLock(lock);
        }
    }
}
