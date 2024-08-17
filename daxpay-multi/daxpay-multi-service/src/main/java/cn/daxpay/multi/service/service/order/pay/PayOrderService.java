package cn.daxpay.multi.service.service.order.pay;

import cn.daxpay.multi.core.exception.TradeNotExistException;
import cn.daxpay.multi.service.dao.order.pay.PayOrderManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.trade.pay.PayCloseService;
import cn.daxpay.multi.service.service.trade.pay.PaySyncService;
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

    /**
     * 同步
     */
    public void sync(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        paySyncService.syncPayOrder(payOrder);
    }

    /**
     * 关闭订单
     */
    public void close(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        payCloseService.closeOrder(payOrder,false);
    }

    /**
     * 撤销订单
     */
    public void cancel(Long id) {
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("支付订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(payOrder.getMchNo(),payOrder.getAppId());
        payCloseService.closeOrder(payOrder,true);
    }

}
