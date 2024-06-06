package cn.daxpay.single.service.core.payment.transfer.service;

import cn.daxpay.single.code.PayChannelEnum;
import cn.daxpay.single.code.RefundStatusEnum;
import cn.daxpay.single.param.payment.transfer.TransferParam;
import cn.daxpay.single.result.transfer.TransferResult;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.service.core.payment.transfer.factory.TransferFactory;
import cn.daxpay.single.service.func.AbsTransferStrategy;
import cn.hutool.extra.spring.SpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    /**
     * 转账
     */
    public TransferResult transfer(TransferParam transferParam){

        // 获取策略
        AbsTransferStrategy transferStrategy = TransferFactory.create(PayChannelEnum.ALI.getCode());
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
    public void successHandler(TransferOrder order){
    }


}
