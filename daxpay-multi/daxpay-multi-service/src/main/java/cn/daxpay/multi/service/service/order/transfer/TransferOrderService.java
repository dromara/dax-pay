package cn.daxpay.multi.service.service.order.transfer;

import cn.bootx.platform.common.spring.util.WebServletUtil;
import cn.daxpay.multi.core.enums.TransferStatusEnum;
import cn.daxpay.multi.core.exception.TradeNotExistException;
import cn.daxpay.multi.core.exception.TradeProcessingException;
import cn.daxpay.multi.core.param.trade.transfer.TransferParam;
import cn.daxpay.multi.service.dao.order.transfer.TransferOrderManager;
import cn.daxpay.multi.service.service.assist.PaymentAssistService;
import cn.daxpay.multi.service.service.trade.transfer.TransferAssistService;
import cn.daxpay.multi.service.service.trade.transfer.TransferService;
import cn.daxpay.multi.service.service.trade.transfer.TransferSyncService;
import cn.hutool.extra.servlet.JakartaServletUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * 转账订单服务
 * @author xxm
 * @since 2024/6/17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransferOrderService {

    private final TransferAssistService transferAssistService;
    private final TransferService transferService;
    private final TransferOrderManager transferOrderManager;
    private final PaymentAssistService paymentAssistService;
    private final TransferSyncService transferSyncService;

    /**
     * 同步
     */
    public void sync(Long id) {
        var transferOrder = transferOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("转账订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(transferOrder.getMchNo(),transferOrder.getAppId());
        // 同步转账订单状态
        transferSyncService.syncTransferOrder(transferOrder);
    }

    /**
     * 转账重试
     */
    public void retry(Long id) {
        var transferOrder = transferOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("转账订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(transferOrder.getMchNo(),transferOrder.getAppId());

        String ip = Optional.ofNullable(WebServletUtil.getRequest())
                .map(JakartaServletUtil::getClientIP)
                .orElse("未知");

        // 构建转账参数并发起
        var transferParam = new TransferParam();
        transferParam.setMchNo(transferOrder.getMchNo());
        transferParam.setAppId(transferOrder.getAppId());
        transferParam.setClientIp(ip);
        transferParam.setReqTime(LocalDateTime.now());
        transferParam.setBizTransferNo(transferOrder.getBizTransferNo());
        transferParam.setAmount(transferOrder.getAmount());
        // 发起转账
        transferService.transfer(transferParam);
    }

    /**
     * 转账关闭
     */
    public void close(Long id) {
        var transferOrder = transferOrderManager.findById(id)
                .orElseThrow(() -> new TradeNotExistException("转账订单不存在"));
        // 初始化商户和应用
        paymentAssistService.initMchAndApp(transferOrder.getMchNo(),transferOrder.getAppId());
        // 更新订单状态
        if (!Objects.equals(TransferStatusEnum.FAIL.getCode(), transferOrder.getStatus())){
            throw new TradeProcessingException("只有失败状态的才可以关闭");
        }
        transferAssistService.close(transferOrder,LocalDateTime.now());
    }
}
