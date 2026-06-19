package cn.daxpay.open.payment.old.pay.service.record.flow;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.exception.operation.OperationFailException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.enums.pay.trade.TradeTypeEnum;
import cn.daxpay.open.payment.old.pay.convert.record.TradeFlowRecordCreateConvert;
import cn.daxpay.open.payment.old.pay.dao.record.flow.TradeFlowRecordManager;
import cn.daxpay.open.payment.old.pay.entity.order.pay.PayOrder;
import cn.daxpay.open.payment.old.pay.entity.record.flow.TradeFlowRecord;
import cn.daxpay.open.payment.old.pay.param.record.TradeFlowRecordQuery;
import cn.daxpay.open.payment.old.pay.result.record.flow.TradeFlowAmountResult;
import cn.daxpay.open.payment.old.pay.result.record.flow.TradeFlowRecordResult;
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

}
