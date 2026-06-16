package org.dromara.daxpay.payment.old.pay.service.trade.transfer;

import org.dromara.daxpay.platform.core.enums.pay.channel.ProductEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.TransferParam;
import org.dromara.daxpay.payment.unipay.result.trade.transfer.TransferResult;
import org.dromara.daxpay.platform.core.util.TradeNoGenerateUtil;
import org.dromara.daxpay.payment.old.pay.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.payment.old.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.old.pay.service.notice.MerchantNoticeService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

/// # 转账辅助服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferAssistService {

    private final TransferOrderManager transferOrderManager;
    private final MerchantNoticeService merchantNoticeService;

    /// 创建转账订单
    @Transactional(rollbackFor = Exception.class)
    public TransferOrder createOrder(TransferParam param) {
        // 创建转账订单
        TransferOrder transferOrder = new TransferOrder()
                .setTransferNo(TradeNoGenerateUtil.transfer())
                .setAmount(param.getAmount())
                .setBizTransferNo(param.getBizTransferNo())
                .setProduct(param.getProduct())
                .setChannel(param.getChannel())
                .setPayeeName(param.getPayeeName())
                .setPayeeAccount(param.getPayeeAccount())
                .setPayeeType(param.getPayeeType())
                .setReason(param.getReason())
                .setStatus(TransferStatusEnum.PROGRESS.getCode())
                .setTitle(param.getTitle())
                .setNotifyUrl(param.getNotifyUrl())
                .setAttach(param.getAttach())
                .setSource(param.getSource())
                .setClientIp(param.getClientIp());
        // 从产品编码派生通道编码
        if (StrUtil.isNotBlank(transferOrder.getProduct()) && StrUtil.isBlank(transferOrder.getChannel())) {
            ProductEnum productEnum = ProductEnum.findByCode(transferOrder.getProduct());
            if (productEnum != null) {
                transferOrder.setChannel(productEnum.getChannel());
            }
        }
        transferOrderManager.save(transferOrder);
        return transferOrder;
    }

    /// 更新转账错误信息
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderByError(TransferOrder transferOrder, String message){
        transferOrder.setErrorMsg(message);
        transferOrder.setStatus(TransferStatusEnum.FAIL.getCode());
        transferOrderManager.updateById(transferOrder);
    }

    /// 转账关闭
    public void close(TransferOrder order, OffsetDateTime finishTime) {
        // 执行策略的关闭方法
        order.setStatus(TransferStatusEnum.CLOSE.getCode())
                .setFinishTime(Optional.ofNullable(finishTime).orElse(OffsetDateTime.now(ZoneOffset.UTC)));
        transferOrderManager.updateById(order);
        merchantNoticeService.registerTransferNotice(order);
    }

    /// 构造
    public TransferResult buildResult(TransferOrder order) {
        return new TransferResult()
                .setTransferNo(order.getTransferNo())
                .setBizTransferNo(order.getBizTransferNo())
                .setStatus(order.getStatus())
                .setTransferBody(order.getTransferBody())
                .setErrorMsg(order.getErrorMsg());
    }
}
