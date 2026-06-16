package org.dromara.daxpay.payment.old.pay.service.record.flow;

import org.dromara.daxpay.platform.common.mybatisplus.util.MpUtil;
import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.rest.param.PageParam;
import org.dromara.daxpay.platform.core.rest.result.PageResult;
import org.dromara.daxpay.platform.core.exception.operation.OperationFailException;
import org.dromara.daxpay.platform.core.code.CommonCode;
import org.dromara.daxpay.platform.core.enums.pay.trade.TradeTypeEnum;
import org.dromara.daxpay.payment.old.pay.convert.record.TradeFlowRecordCreateConvert;
import org.dromara.daxpay.payment.old.pay.dao.record.flow.TradeFlowRecordManager;
import org.dromara.daxpay.payment.old.pay.entity.order.pay.PayOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.refund.RefundOrder;
import org.dromara.daxpay.payment.old.pay.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.payment.old.pay.entity.record.flow.TradeFlowRecord;
import org.dromara.daxpay.payment.old.pay.param.record.TradeFlowRecordQuery;
import org.dromara.daxpay.payment.old.pay.result.record.flow.TradeFlowAmountResult;
import org.dromara.daxpay.payment.old.pay.result.record.flow.TradeFlowRecordResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 交易流水记录服务类
///
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeFlowRecordService {
    private final TradeFlowRecordManager tradeFlowRecordManager;

    /// 分页
    public PageResult<TradeFlowRecordResult> page(PageParam pageParam, TradeFlowRecordQuery query){
        return MpUtil.toPageResult(tradeFlowRecordManager.page(pageParam, query));
    }

    /// 查询详情
    public TradeFlowRecordResult findById(Long id){
        return tradeFlowRecordManager.findById(id).map(TradeFlowRecord::toResult)
                .orElseThrow(()->new DataNotExistException("error.payment.order.tradeFlowNotExist"));
    }

    /// 查询各类金额汇总
    public TradeFlowAmountResult summary(TradeFlowRecordQuery query){
        var result = new TradeFlowAmountResult();
        result.setIncomeAmount(tradeFlowRecordManager.getTotalAmount(query, TradeTypeEnum.PAY));
        result.setRefundAmount(tradeFlowRecordManager.getTotalAmount(query, TradeTypeEnum.REFUND));
        result.setTransferAmount(tradeFlowRecordManager.getTotalAmount(query, TradeTypeEnum.TRANSFER));
        return result;
    }

    /// 支付记录
    public void savePay(PayOrder payOrder){
        TradeFlowRecord tradeFlowRecord = TradeFlowRecordCreateConvert.CONVERT.fromPayOrder(payOrder);
        tradeFlowRecord.setTradeNo(payOrder.getOrderNo());
        tradeFlowRecord.setBizTradeNo(payOrder.getBizOrderNo());
        tradeFlowRecord.setOutTradeNo(payOrder.getOutOrderNo());
        tradeFlowRecord.setType("pay");
        tradeFlowRecordManager.save(tradeFlowRecord);
    }

    /// 退款记账
    public void saveRefund(RefundOrder refundOrder){
        TradeFlowRecord tradeFlowRecord = TradeFlowRecordCreateConvert.CONVERT.fromRefundOrder(refundOrder);
        tradeFlowRecord.setTradeNo(refundOrder.getRefundNo());
        tradeFlowRecord.setBizTradeNo(refundOrder.getBizRefundNo());
        tradeFlowRecord.setOutTradeNo(refundOrder.getOutRefundNo());
        tradeFlowRecord.setType("refund");
        tradeFlowRecordManager.save(tradeFlowRecord);
    }

    /// 转账记账
    public void saveTransfer(TransferOrder transferOrder){
        TradeFlowRecord tradeFlowRecord = TradeFlowRecordCreateConvert.CONVERT.fromTransferOrder(transferOrder);
        tradeFlowRecord.setTradeNo(transferOrder.getTransferNo());
        tradeFlowRecord.setBizTradeNo(transferOrder.getBizTransferNo());
        tradeFlowRecord.setOutTradeNo(transferOrder.getOutTransferNo());
        tradeFlowRecord.setType("transfer");
        tradeFlowRecordManager.save(tradeFlowRecord);
    }

    
    

}
