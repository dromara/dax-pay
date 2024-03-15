package cn.bootx.platform.daxpay.service.core.order.pay.service;

import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.service.core.order.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.service.core.order.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.service.core.payment.pay.service.PayExpiredTimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private final PayExpiredTimeService expiredTimeService;

    // 支付完成常量集合
    private final List<String> ORDER_FINISH = Arrays.asList(PayStatusEnum.CLOSE.getCode(), PayStatusEnum.SUCCESS.getCode());

    /**
     * 查询
     */
    public Optional<PayOrder> findById(Long id){
        return payOrderManager.findById(id);
    }

    /**
     * 新增
     */
    public void save(PayOrder payOrder){
        payOrderManager.save(payOrder);
        // 异步支付需要添加订单超时任务记录
        if (payOrder.isAsyncPay()){
            expiredTimeService.registerExpiredTime(payOrder);
        }
    }

    /**
     * 更新
     */
    public void updateById(PayOrder payOrder){
        // 如果是异步支付且支付订单完成, 需要删除订单超时任务记录
        if (payOrder.isAsyncPay() && ORDER_FINISH.contains(payOrder.getStatus())){
            expiredTimeService.cancelExpiredTime(payOrder.getId());
        }
        payOrderManager.updateById(payOrder);
    }

    /**
     * 使用强制更新
     */
    public void updateForceById(PayOrder payOrder){
        // 如果是异步支付且支付订单完成, 需要删除订单超时任务记录
        if (payOrder.isAsyncPay() && ORDER_FINISH.contains(payOrder.getStatus())){
            expiredTimeService.cancelExpiredTime(payOrder.getId());
        }
        payOrder.setVersion(null);
        payOrderManager.updateForceById(payOrder);
    }

}
