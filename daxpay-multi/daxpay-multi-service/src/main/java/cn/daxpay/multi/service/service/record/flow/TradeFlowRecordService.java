package cn.daxpay.multi.service.service.record.flow;

import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.core.exception.DataNotExistException;
import cn.bootx.platform.core.rest.param.PageParam;
import cn.bootx.platform.core.rest.result.PageResult;
import cn.daxpay.multi.core.enums.TradeFlowTypeEnum;
import cn.daxpay.multi.service.dao.record.flow.TradeFlowRecordManager;
import cn.daxpay.multi.service.entity.order.pay.PayOrder;
import cn.daxpay.multi.service.entity.order.refund.RefundOrder;
import cn.daxpay.multi.service.entity.record.flow.TradeFlowRecord;
import cn.daxpay.multi.service.param.record.TradeFlowRecordQuery;
import cn.daxpay.multi.service.result.record.flow.TradeFlowRecordResult;
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
     * 支付记账
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

}

