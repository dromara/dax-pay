package org.dromara.daxpay.service.service.record.flow;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import org.dromara.daxpay.core.enums.TradeFlowTypeEnum;
import org.dromara.daxpay.core.enums.TradeTypeEnum;
import org.dromara.daxpay.service.dao.record.flow.TradeFlowRecordManager;
import org.dromara.daxpay.service.entity.order.pay.PayOrder;
import org.dromara.daxpay.service.entity.order.refund.RefundOrder;
import org.dromara.daxpay.service.entity.order.transfer.TransferOrder;
import org.dromara.daxpay.service.entity.record.flow.TradeFlowRecord;
import org.dromara.daxpay.service.param.record.TradeFlowRecordQuery;
import org.dromara.daxpay.service.result.record.flow.TradeFlowAmountResult;
import org.dromara.daxpay.service.result.record.flow.TradeFlowRecordResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 交易流水记录服务类
 * @author xxm
 * @since 2024/4/21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradeFlowRecordService {
    private final TradeFlowRecordManager tradeFlowRecordManager;

    /**
     * 分页
     */
    public PageResult<TradeFlowRecordResult> page(PageParam pageParam, TradeFlowRecordQuery query){
        return MpUtil.toPageResult(tradeFlowRecordManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public TradeFlowRecordResult findById(Long id){
        return tradeFlowRecordManager.findById(id).map(TradeFlowRecord::toResult)
                .orElseThrow(()->new DataNotExistException("交易流水记录不存在"));
    }

    /**
     * 查询各类金额汇总
     */
    public TradeFlowAmountResult summary(TradeFlowRecordQuery query){
        var result = new TradeFlowAmountResult();
        // 支付
        result.setIncomeAmount(tradeFlowRecordManager.getTotalAmount(query, TradeTypeEnum.PAY));
        // 退款
        result.setRefundAmount(tradeFlowRecordManager.getTotalAmount(query, TradeTypeEnum.REFUND));
        // 转账
        result.setTransferAmount(tradeFlowRecordManager.getTotalAmount(query, TradeTypeEnum.TRANSFER));
        return result;
    }


    /**
     * 支付记录
     */
    public void savePay(PayOrder payOrder){
        TradeFlowRecord tradeFlowRecord = new TradeFlowRecord()
                .setTradeNo(payOrder.getOrderNo())
                .setBizTradeNo(payOrder.getBizOrderNo())
                .setOutTradeNo(payOrder.getOutOrderNo())
                .setChannel(payOrder.getChannel())
                .setTitle(payOrder.getTitle())
                .setType(TradeFlowTypeEnum.PAY.getCode())
                .setAmount(payOrder.getAmount());
        tradeFlowRecordManager.save(tradeFlowRecord);
    }

    /**
     * 退款记账
     */
    public void saveRefund(RefundOrder refundOrder){
        TradeFlowRecord tradeFlowRecord = new TradeFlowRecord()
                .setTradeNo(refundOrder.getRefundNo())
                .setBizTradeNo(refundOrder.getBizRefundNo())
                .setOutTradeNo(refundOrder.getOutRefundNo())
                .setChannel(refundOrder.getChannel())
                .setTitle(refundOrder.getTitle())
                .setType(TradeFlowTypeEnum.REFUND.getCode())
                .setAmount(refundOrder.getAmount());
        tradeFlowRecordManager.save(tradeFlowRecord);
    }

    /**
     * 转账记账
     */
    public void saveTransfer(TransferOrder transferOrder){
        TradeFlowRecord tradeFlowRecord = new TradeFlowRecord()
                .setTradeNo(transferOrder.getTransferNo())
                .setBizTradeNo(transferOrder.getBizTransferNo())
                .setOutTradeNo(transferOrder.getOutTransferNo())
                .setChannel(transferOrder.getChannel())
                .setTitle(transferOrder.getTitle())
                .setType(TradeFlowTypeEnum.TRANSFER.getCode())
                .setAmount(transferOrder.getAmount());
        tradeFlowRecordManager.save(tradeFlowRecord);
    }

}

