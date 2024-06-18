package cn.daxpay.single.service.core.payment.transfer.service;

import cn.daxpay.single.core.code.RefundStatusEnum;
import cn.daxpay.single.core.param.payment.transfer.TransferParam;
import cn.daxpay.single.core.result.transfer.TransferResult;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.transfer.dao.TransferOrderManager;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.func.AbsTransferStrategy;
import cn.daxpay.single.service.util.PayStrategyFactory;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 转账服务
 * @author xxm
 * @since 2024/3/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferAssistService transferAssistService;

    private final TransferOrderManager transferOrderManager;

    /**
     * 转账
     */
    public TransferResult transfer(TransferParam transferParam){

        // 获取策略
        AbsTransferStrategy transferStrategy = PayStrategyFactory.create(transferParam.getChannel(),AbsTransferStrategy.class);
        // 检查转账参数
        transferStrategy.doValidateParam(transferParam);
        // 创建转账订单并设置
        TransferOrder order = transferAssistService.createOrder(transferParam);
        transferStrategy.setTransferOrder(order);
        // 执行预处理
        transferStrategy.doBeforeHandler();
        try {
            // 执行转账策略
            transferStrategy.doTransferHandler();
        } catch (Exception e) {
            log.error("转账出现错误", e);
            // 记录处理失败状态
            PaymentContextLocal.get().getRefundInfo().setStatus(RefundStatusEnum.FAIL);
            // 更新退款失败的记录
            transferAssistService.updateOrderByError(order);
            return transferAssistService.buildResult(order);
        }
        SpringUtil.getBean(this.getClass()).successHandler(order);
        return transferAssistService.buildResult(order);
    }

    /**
     * 成功处理
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void successHandler(TransferOrder order){
        order.setStatus(RefundStatusEnum.SUCCESS.getCode());
        transferOrderManager.updateById(order);
    }
}
