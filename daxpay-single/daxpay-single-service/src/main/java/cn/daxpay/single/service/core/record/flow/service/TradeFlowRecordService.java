package cn.daxpay.single.service.core.record.flow.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.single.service.code.TradeFlowRecordTypeEnum;
import cn.daxpay.single.service.core.order.pay.entity.PayOrder;
import cn.daxpay.single.service.core.order.refund.entity.RefundOrder;
import cn.daxpay.single.service.core.record.flow.dao.TradeFlowRecordManager;
import cn.daxpay.single.service.core.record.flow.entity.TradeFlowRecord;
import cn.daxpay.single.service.dto.record.flow.TradeFlowRecordDto;
import cn.daxpay.single.service.param.record.TradeFlowRecordQuery;
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
    public PageResult<TradeFlowRecordDto> page(PageParam pageParam, TradeFlowRecordQuery query){
        return MpUtil.convert2DtoPageResult(tradeFlowRecordManager.page(pageParam, query));
    }

    /**
     * 查询详情
     */
    public TradeFlowRecordDto findById(Long id){
        return tradeFlowRecordManager.findById(id).map(TradeFlowRecord::toDto)
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
                .setType(TradeFlowRecordTypeEnum.PAY.getCode())
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
                .setType(TradeFlowRecordTypeEnum.PAY.getCode())
                .setAmount(refundOrder.getAmount());
        tradeFlowRecordManager.save(tradeFlowRecord);
    }

}

