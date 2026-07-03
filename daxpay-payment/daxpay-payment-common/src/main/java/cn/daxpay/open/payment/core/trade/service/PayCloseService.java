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
@Slf4j
@Service
@RequiredArgsConstructor
public class PayCloseService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;
    private final PayUniHandleService payUniHandleService;
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
            if (Objects.equals(PayFundStatusEnum.INIT.getCode(), trade.getStatus())) {
                payUniHandleService.payClose(trade, normalOrder);
            } else {
                PayStrategyContext context = new PayStrategyContext()
                        .setContainer(normalOrder)
                        .setTrade(trade);
                AbsPayCloseStrategy strategy = PaymentStrategyFactory.createByProduct(
                        trade.getProduct(), AbsPayCloseStrategy.class);
                strategy.doBeforeClose(context);
                strategy.doClose(context, useCancel);
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
