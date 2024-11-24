package org.dromara.daxpay.service.service.order.pay;

import jakarta.validation.constraints.NotNull;
import org.dromara.daxpay.core.exception.TradeNotExistException;
import org.dromara.daxpay.core.param.allocation.transaction.AllocationParam;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.dao.order.pay.PayOrderManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.service.allocation.AllocationService;
import org.dromara.daxpay.service.service.assist.PaymentAssistService;
import org.dromara.daxpay.service.service.trade.pay.PayCloseService;
import org.dromara.daxpay.service.service.trade.pay.PaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 支付订单服务
 * @author xxm
 * @since 2023/12/18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderService {
    private final PayOrderManager payOrderManager;
    private final PaySyncService paySyncService;

    private final PaymentAssistService paymentAssistService;
    private final PayCloseService payCloseService;
    private final AllocationService allocationService;

    /**
     * 同步
     */
    public void sync(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(payOrder.getAppId());
        paySyncService.syncPayOrder(payOrder);
    }

    /**
     * 关闭订单
     */
    public void close(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(payOrder.getAppId());
        payCloseService.closeOrder(payOrder,false);
    }

    /**
     * 撤销订单
     */
    public void cancel(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户应用
        paymentAssistService.initMchApp(payOrder.getAppId());
        payCloseService.closeOrder(payOrder,true);
    }

    /**
     * 分账
     */
    public void allocation(@NotNull(message = "支付订单id不能为空") Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        paymentAssistService.initMchApp(payOrder.getAppId());
        AllocationParam param = new AllocationParam()
                .setBizAllocNo("B"+TradeNoGenerateUtil.allocation());
        param.setAppId(payOrder.getAppId());
        allocationService.allocation(param, payOrder);

    }
}
