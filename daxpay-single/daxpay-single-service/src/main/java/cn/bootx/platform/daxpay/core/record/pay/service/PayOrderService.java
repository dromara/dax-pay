package cn.bootx.platform.daxpay.core.record.pay.service;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import cn.bootx.platform.daxpay.common.entity.OrderRefundableInfo;
import cn.bootx.platform.daxpay.core.record.pay.dao.PayOrderManager;
import cn.bootx.platform.daxpay.core.record.pay.entity.PayOrder;
import cn.bootx.platform.daxpay.core.timeout.service.PayExpiredTimeService;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    private final List<String> ORDER_FINISH = Arrays.asList(PayStatusEnum.CLOSE.getCode(),PayStatusEnum.TIMEOUT.getCode(), PayStatusEnum.SUCCESS.getCode());

    /**
     * 根据id查询
     */
    public Optional<PayOrder> findById(Long paymentId) {
        return payOrderManager.findById(paymentId);
    }

    /**
     * 根据业务号查询
     */
    public Optional<PayOrder> findByBusinessNo(String businessNo) {
        return payOrderManager.findByBusinessNo(businessNo);
    }

    /**
     * 新增
     */
    public void save(PayOrder payOrder){
        // 异步支付需要添加订单超时任务记录
        if (payOrder.isAsyncPay()){
            expiredTimeService.registerExpiredTime(payOrder);
        }

        payOrderManager.save(payOrder);
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
     * 退款成功处理, 更新可退款信息 不要进行持久化
     */
    public void updateRefundSuccess(PayOrder payment, int amount, PayChannelEnum payChannelEnum) {
        // 删除旧有的退款记录, 替换退款完的新的
        List<OrderRefundableInfo> refundableInfos = payment.getRefundableInfos();
        OrderRefundableInfo refundableInfo = refundableInfos.stream()
                .filter(o -> Objects.equals(o.getChannel(), payChannelEnum.getCode()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("退款数据不存在"));
        refundableInfos.remove(refundableInfo);
        refundableInfo.setAmount(refundableInfo.getAmount() - amount);
        refundableInfos.add(refundableInfo);
        payment.setRefundableInfos(refundableInfos);
    }
}
