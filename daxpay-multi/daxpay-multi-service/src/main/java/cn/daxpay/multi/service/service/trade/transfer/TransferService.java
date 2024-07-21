package cn.daxpay.multi.service.service.trade.transfer;

import cn.daxpay.multi.core.enums.RefundStatusEnum;
import cn.daxpay.multi.core.result.trade.TransferResult;
import cn.daxpay.multi.service.common.context.TransferLocal;
import cn.daxpay.multi.service.common.local.PaymentContextLocal;
import cn.daxpay.multi.service.dao.order.transfer.TransferOrderManager;
import cn.daxpay.multi.service.entity.order.transfer.TransferOrder;
import cn.daxpay.multi.service.param.order.transfer.TransferParam;
import cn.daxpay.multi.service.strategy.AbsTransferStrategy;
import cn.daxpay.multi.service.util.PayStrategyFactory;
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
        AbsTransferStrategy transferStrategy = PayStrategyFactory.create(transferParam.getChannel(), AbsTransferStrategy.class);
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
            PaymentContextLocal.get().getErrorInfo().setException(e);
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
        TransferLocal transferInfo = PaymentContextLocal.get()
                .getTransferInfo();
        order.setStatus(transferInfo.getStatus().getCode())
                .setSuccessTime(transferInfo.getFinishTime())
                .setOutTransferNo(transferInfo.getOutTransferNo());
        transferOrderManager.updateById(order);
    }
}
