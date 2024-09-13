package cn.daxpay.single.service.core.payment.transfer.service;

import cn.daxpay.single.core.code.TransferStatusEnum;
import cn.daxpay.single.core.param.payment.transfer.TransferParam;
import cn.daxpay.single.core.result.transfer.TransferResult;
import cn.daxpay.single.service.common.context.ErrorInfoLocal;
import cn.daxpay.single.service.common.local.PaymentContextLocal;
import cn.daxpay.single.service.core.order.transfer.dao.TransferOrderManager;
import cn.daxpay.single.service.core.order.transfer.entity.TransferOrder;
import cn.daxpay.single.core.util.TradeNoGenerateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 转账辅助服务
 * @author xxm
 * @since 2024/6/6
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferAssistService {

    private final TransferOrderManager transferOrderManager;

    /**
     * 创建转账订单
     */
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder createOrder(TransferParam param) {
        // 创建转账订单
        TransferOrder transferOrder = new TransferOrder()
                .setTransferNo(TradeNoGenerateUtil.transfer())
                .setAmount(param.getAmount())
                .setBizTransferNo(param.getBizTransferNo())
                .setChannel(param.getChannel())
                .setPayeeName(param.getPayeeName())
                .setPayeeAccount(param.getPayeeAccount())
                .setPayeeType(param.getPayeeType())
                .setReason(param.getReason())
                .setStatus(TransferStatusEnum.TRANSFERRING.getCode())
                .setTitle(param.getTitle())
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setClientIp(param.getClientIp());
        transferOrderManager.save(transferOrder);
        return transferOrder;
    }

    /**
     * 更新转账订单错误信息
     */
    public void updateOrderByError(TransferOrder order) {
        ErrorInfoLocal errorInfo = PaymentContextLocal.get().getErrorInfo();
        order.setStatus(TransferStatusEnum.FAIL.getCode())
                .setErrorMsg(errorInfo.getErrorMsg())
                .setErrorCode(String.valueOf(errorInfo.getErrorCode()));
        transferOrderManager.updateById(order);
    }

    /**
     * 构造
     */
    public TransferResult buildResult(TransferOrder order) {
        return new TransferResult()
                .setTransferNo(order.getTransferNo())
                .setBizTransferNo(order.getBizTransferNo())
                .setStatus(order.getStatus());
    }
}
