package org.dromara.daxpay.payment.pay.service.order.transfer;

import org.dromara.daxpay.platform.common.spring.util.WebServletUtil;

import org.dromara.daxpay.platform.core.code.DaxPayErrorCode;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.code.DaxPayCode;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeSourceEnum;
import org.dromara.daxpay.platform.core.enums.pay.transfer.TransferStatusEnum;
import org.dromara.daxpay.payment.pay.exception.TradeNotExistException;
import org.dromara.daxpay.payment.pay.exception.TradeProcessingException;
import org.dromara.daxpay.payment.unipay.param.trade.transfer.TransferParam;
import org.dromara.daxpay.payment.pay.dao.order.transfer.TransferOrderManager;
import org.dromara.daxpay.payment.pay.service.assist.PaymentAssistService;
import org.dromara.daxpay.payment.pay.service.trade.transfer.TransferAssistService;
import org.dromara.daxpay.payment.pay.service.trade.transfer.TransferService;
import org.dromara.daxpay.payment.pay.service.trade.transfer.TransferSyncService;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/// # 转账订单服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderService {

    private final TransferAssistService transferAssistService;
    private final TransferService transferService;
    private final TransferOrderManager transferOrderManager;
    private final PaymentAssistService paymentAssistService;
    private final TransferSyncService transferSyncService;

    /// 同步
    public void sync(Long id) {
        var transferOrder = transferOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.transferOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(transferOrder.getMchNo(),transferOrder.getAppId());
        // 同步转账订单状态
        transferSyncService.syncTransferOrder(transferOrder);
    }

    /// 转账重试
    public void retry(Long id) {
        var transferOrder = transferOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.transferOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(transferOrder.getMchNo(),transferOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("127.0.0.1");

        // 构建转账参数并发起
        var transferParam = new TransferParam();
        transferParam.setMchNo(transferOrder.getMchNo());
        transferParam.setAppId(transferOrder.getAppId());
        transferParam.setClientIp(ip);
        transferParam.setReqTime(LocalDateTime.now());
        transferParam.setBizTransferNo(transferOrder.getBizTransferNo());
        transferParam.setAmount(transferOrder.getAmount());
        transferParam.setSource(TradeSourceEnum.USER.getCode());
        // 发起转账
        transferService.transfer(transferParam);
    }

    /// 转账关闭
    public void close(Long id) {
        var transferOrder = transferOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("error.payment.order.transferOrderNotExist"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(transferOrder.getMchNo(),transferOrder.getAppId());
        // 更新订单状态
        if (!Objects.equals(TransferStatusEnum.FAIL.getCode(), transferOrder.getStatus())){
            // 只有失败状态的才可以关闭
            throw new TradeProcessingException(DaxPayErrorCode.TRADE_PROCESSING, "pay.error.transfer.onlyFailRetry");
        }
        transferAssistService.close(transferOrder,LocalDateTime.now());
    }

    
    
}
