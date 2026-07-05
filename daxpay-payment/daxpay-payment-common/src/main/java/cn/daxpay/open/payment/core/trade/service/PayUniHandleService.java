package cn.daxpay.open.payment.core.trade.service;

import cn.daxpay.open.payment.common.enums.NormalPayOrderStatusEnum;
import cn.daxpay.open.payment.common.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.core.trade.dao.NormalPayOrderManager;
import cn.daxpay.open.payment.core.trade.entity.NormalPayOrder;
import cn.daxpay.open.payment.core.trade.dao.PayTradeManager;
import cn.daxpay.open.payment.core.trade.entity.PayTrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/// # 交易统一处理服务
///
/// 支付成功/失败/关闭后的统一处理逻辑
@Slf4j
@Service
@RequiredArgsConstructor
public class PayUniHandleService {

    private final PayTradeManager payTradeManager;
    private final NormalPayOrderManager payNormalOrderManager;

    /// 支付发起后处理（参考商业版 TradeUniHandleService.payAfterHandel）
    /// 不论是否完成都更新交易单; 仅资金状态为 SUCCESS 时同步容器为 PAID。
    /// 异步支付(complete=false)时容器保持 WAIT_PAY, 交易单保持 PROCESSING。
    public void payAfterHandel(PayTrade trade) {
        payTradeManager.updateById(trade);
        if (Objects.equals(trade.getStatus(), PayFundStatusEnum.SUCCESS.getCode())) {
            NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                    .orElse(null);
            if (normalOrder != null) {
                normalOrder.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
                normalOrder.setPayTime(trade.getPayTime());
                payNormalOrderManager.updateById(normalOrder);
            }
        }
    }

    /// 支付成功后续处理（同步冗余时间线到容器）
    public void paySuccess(PayTrade trade) {
        NormalPayOrder normalOrder = payNormalOrderManager.findById(trade.getContainerId())
                .orElse(null);
        if (normalOrder != null) {
            normalOrder.setStatus(NormalPayOrderStatusEnum.PAID.getCode());
            normalOrder.setPayTime(trade.getPayTime());
            payNormalOrderManager.updateById(normalOrder);
        }
        payTradeManager.updateById(trade);
    }

    /// 支付失败处理（同步冗余时间线到容器）
    public void payFail(PayTrade trade, NormalPayOrder normalOrder, String errMsg) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.FAIL.getCode());
        trade.setErrorMsg(errMsg);
        trade.setCloseTime(now);
        normalOrder.setStatus(NormalPayOrderStatusEnum.CLOSED.getCode());
        normalOrder.setCloseTime(now);
        payTradeManager.updateById(trade);
        payNormalOrderManager.updateById(normalOrder);
    }

    /// 支付关闭处理（同步冗余时间线到容器）
    /// @param useCancel true=撤销(资金态置 CANCEL), false=关闭(资金态置 CLOSE); 容器统一置 CLOSED
    public void payClose(PayTrade trade, NormalPayOrder normalOrder, boolean useCancel) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(useCancel
                ? PayFundStatusEnum.CANCEL.getCode()
                : PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        normalOrder.setStatus(NormalPayOrderStatusEnum.CLOSED.getCode());
        normalOrder.setCloseTime(now);
        payTradeManager.updateById(trade);
        payNormalOrderManager.updateById(normalOrder);
    }

    /// 支付超时关闭处理
    /// 资金态置 CLOSE(与普通关闭等价), 容器态置 EXPIRED(区分业务语义),
    /// 供 MQ 延时关单 / 兜底定时任务 / 同步发现超时 三条路径统一调用。
    public void payTimeout(PayTrade trade, NormalPayOrder normalOrder) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        trade.setStatus(PayFundStatusEnum.CLOSE.getCode());
        trade.setCloseTime(now);
        normalOrder.setStatus(NormalPayOrderStatusEnum.EXPIRED.getCode());
        normalOrder.setCloseTime(now);
        payTradeManager.updateById(trade);
        payNormalOrderManager.updateById(normalOrder);
    }
}
