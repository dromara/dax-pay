package cn.bootx.platform.daxpay.service.core.order.reconcile.service;

import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.sequence.func.Sequence;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.service.core.order.reconcile.dao.PayReconcileOrderManager;
import cn.bootx.platform.daxpay.service.core.order.reconcile.entity.PayReconcileOrder;
import cn.hutool.core.date.DatePattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

/**
 * 支付对账订单服务
 * @author xxm
 * @since 2024/1/20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayReconcileOrderService {
    private final PayReconcileOrderManager reconcileOrderManager;
    private final Sequence sequence;

    /**
     * 更新, 开启一个新事务进行更新
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void update(PayReconcileOrder order){
        reconcileOrderManager.updateById(order);
    }

    /**
     * 根据Id查询
     */
    public Optional<PayReconcileOrder> findById(Long id){
        return reconcileOrderManager.findById(id);
    }

    /**
     * 创建对账订单
     */
    public PayReconcileOrder create(LocalDate date,String channel) {
        // 获取通道枚举
        PayChannelEnum channelEnum = PayChannelEnum.findByCode(channel);
        // 判断是否为为异步通道
        if (!PayChannelEnum.ASYNC_TYPE.contains(channelEnum)){
            throw new PayFailureException("不支持对账的通道, 请检查");
        }
        // 生成批次号
        String format = LocalDateTimeUtil.format(date, DatePattern.PURE_DATE_PATTERN);
        String key = channelEnum.getReconcilePrefix()+format;
        String seqNo = key + this.genSeqNo(key);

        PayReconcileOrder order = new PayReconcileOrder()
                .setBatchNo(seqNo)
                .setChannel(channel)
                .setDate(date);
        reconcileOrderManager.save(order);
        return order;
    }


    /**
     * 生成批次号
     * 规则：通道简称 + yyyyMMdd + 两位流水号
     * 例子：wx2024012001、ali2024012002
     */
    private String genSeqNo(String key){
        long next = sequence.next(key);
        return String.format("%02d",next);
    }
}
