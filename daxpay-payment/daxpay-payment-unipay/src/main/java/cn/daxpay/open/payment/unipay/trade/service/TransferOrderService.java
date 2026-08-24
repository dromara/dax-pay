package cn.daxpay.open.payment.unipay.trade.service;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.payment.trade.transfer.dao.TransferTradeManager;
import cn.daxpay.open.payment.trade.transfer.entity.TransferTrade;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferConfirmUrlService;
import cn.daxpay.open.payment.trade.transfer.runtime.service.TransferStartService;
import cn.daxpay.open.payment.unipay.param.trade.transfer.TransferParam;
import cn.daxpay.open.payment.unipay.result.trade.transfer.TransferCreateResult;
import cn.daxpay.open.payment.unipay.trade.convert.UnipayTransferConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 转账发起服务(对外)
///
/// 对外统一转账的编排入口: 对外签名入参 → 内部编排参数, 委托核心 [TransferStartService] 建单调通道,
/// 再回查凭证组装发起响应。
/// 核心层只认内部 [cn.daxpay.open.payment.trade.transfer.param.TransferParam], 对外签名字段(mchNo/appId/sign)
/// 与金额单位(分→元)在转换器中处理; 发起失败由核心置失败单后以异常响应返回(可复用原商户转账号重试)。
/// 与查询/同步的 [TransferOrderQueryService] / [TransferOrderSyncService] 并列。
@Service
@RequiredArgsConstructor
public class TransferOrderService {

    private final TransferStartService transferStartService;
    private final TransferTradeManager transferTradeManager;
    private final TransferConfirmUrlService transferConfirmUrlService;

    /// 发起转账
    public TransferCreateResult create(TransferParam param) {
        // channel 为核心发起服务的独立入参(内部编排参数不含通道字段)
        String transferNo = transferStartService.start(param.getChannel(),
                UnipayTransferConvert.CONVERT.toRuntime(param));
        // 回查凭证取状态(正常返回状态为 processing 或 success; 发起失败走异常响应, 不会到此处)
        TransferTrade trade = transferTradeManager.findByTradeNo(transferNo)
                .orElseThrow(() -> new DataNotExistException("pay.error.transfer.notFound"));
        TransferCreateResult result = new TransferCreateResult()
                .setTransferNo(trade.getTradeNo())
                .setBizTransferNo(trade.getBizTransferNo())
                .setStatus(trade.getStatus());
        // 微信转账待收款人确认时填充确认收款链接(与商户端详情行为一致)
        if ("wechat".equals(trade.getContainerChannel())
                && PayFundStatusEnum.PROCESSING.getCode().equals(trade.getStatus())) {
            result.setConfirmUrl(transferConfirmUrlService.buildConfirmUrl(transferNo));
        }
        return result;
    }
}
