package cn.daxpay.open.payment.old.pay.service.order.pay;

import cn.daxpay.open.platform.core.exception.operation.OperationFailException;

import cn.daxpay.open.payment.old.pay.dao.order.pay.PayOrderManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.exception.TradeNotExistException;
import cn.daxpay.open.payment.common.service.MerchantContextLoader;
import cn.daxpay.open.payment.old.pay.service.trade.pay.PayCloseService;
import cn.daxpay.open.payment.old.pay.service.trade.pay.PaySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cn.daxpay.open.platform.core.code.CommonCode;

/// # 支付订单服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class PayOrderService {
    private final PayOrderManager payOrderManager;
    private final PaySyncService paySyncService;

    private final MerchantContextLoader merchantContextLoader;
    private final PayCloseService payCloseService;

    /// 同步
    public void sync(Long id) {
        // 订单: 支付订单不存在
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        // 初始化商户和应用
        merchantContextLoader.initMch(payOrder.getMchNo());
        paySyncService.syncPayOrder(payOrder);
    }

    /// 关闭订单
    public void close(Long id) {
        // 订单: 支付订单不存在
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        // 初始化商户和应用
        merchantContextLoader.initMch(payOrder.getMchNo());
        payCloseService.closeOrder(payOrder,false);
    }

    /// 撤销订单
    public void cancel(Long id) {
        // 订单: 支付订单不存在
        PayOrder payOrder = payOrderManager.findById(id).orElseThrow(() -> new TradeNotExistException("error.payment.order.payOrderNotExist"));
        // 初始化商户和应用
        merchantContextLoader.initMch(payOrder.getMchNo());
        payCloseService.closeOrder(payOrder,true);
    }

    
    
}
