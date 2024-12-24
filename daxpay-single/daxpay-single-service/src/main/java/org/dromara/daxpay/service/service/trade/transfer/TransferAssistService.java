package org.dromara.daxpay.service.service.trade.transfer;

import org.dromara.daxpay.core.enums.TransferStatusEnum;
import org.dromara.daxpay.core.param.trade.transfer.TransferParam;
import org.dromara.daxpay.core.result.trade.transfer.TransferResult;
import org.dromara.daxpay.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.service.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.service.notice.MerchantNoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private final MerchantNoticeService merchantNoticeService;

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
                .setStatus(TransferStatusEnum.PROGRESS.getCode())
                .setTitle(param.getTitle())
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setClientIp(param.getClientIp());
        transferOrderManager.save(transferOrder);
        return transferOrder;
    }

    /**
     * 更新转账错误信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByError(TransferOrder transferOrder, String message){
        transferOrder.setErrorMsg(message);
        transferOrder.setStatus(TransferStatusEnum.FAIL.getCode());
        transferOrderManager.updateById(transferOrder);
    }

    /**
     *  转账关闭
     */
    public void close(TransferOrder order, LocalDateTime finishTime) {
        // 执行策略的关闭方法
        order.setStatus(TransferStatusEnum.CLOSE.getCode())
                .setFinishTime(Optional.ofNullable(finishTime).orElse(LocalDateTime.now()));
        transferOrderManager.updateById(order);
        merchantNoticeService.registerTransferNotice(order);
    }

    /**
     * 构造
     */
    public TransferResult buildResult(TransferOrder order) {
        return new TransferResult()
                .setTransferNo(order.getTransferNo())
                .setBizTransferNo(order.getBizTransferNo())
                .setStatus(order.getStatus())
                .setErrorMsg(order.getErrorMsg());
    }
}
